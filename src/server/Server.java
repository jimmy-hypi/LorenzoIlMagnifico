package server;

import command.toclient.AskForReconnectionCommand;
import command.toserver.ReconnectionAnswerCommand;
import constant.FileConstants;
import constant.NetworkConstants;
import server.controller.InitialTimer;
import server.controller.MatchHandler;
import server.rmi.ServerRMIListener;
import server.socket.ServerSocketListener;
import utils.User;
import utils.UsersCreator;

import java.io.*;
import java.rmi.RemoteException;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Server main duties:
 * - Managing of Listeners
 * - Create Match instances and run them
 * - Managing of disconnecting clients
 */
public class Server implements Runnable, ServerInterface {

    private Deque<ClientHandler> waitingClients;
    private Deque<MatchHandler> createdMatches;
    private Map<MatchHandler, Future> futures;
    private ServerSocketListener socketListener;
    private ServerRMIListener rmiListener;
    private ExecutorService executor;
    private BufferedReader inKeyboard;
    private int port;
    // Singleton pattern
    private static Server instance;
    private Thread timer;
    private boolean closeServer = false;

    private Server(int port) {
        this.port = port;
        inKeyboard = new BufferedReader(new InputStreamReader(System.in));
        futures = new HashMap<MatchHandler, Future>();
    }

    public static synchronized Server getInstance() {
        if (instance == null) {
            return new Server(NetworkConstants.PORT);
        } else {
            return instance;
        }
    }

    @Override
    public void run() {
        executor = Executors.newCachedThreadPool();
        waitingClients = new ConcurrentLinkedDeque<ClientHandler>();
        createdMatches = new ConcurrentLinkedDeque<MatchHandler>();
        socketListener = new ServerSocketListener(this, port);
        executor.submit(socketListener);
        rmiListener = new ServerRMIListener(this);
        executor.submit(rmiListener);
        while (!closeServer) {
            String input = null;
            try {
                input = inKeyboard.readLine();
            } catch (IOException e) {
            }
            if ("M".equals(input) || "m".equals(input)) {
                closeServer = true;
                closeServer();
            }
        }
    }

    /**
     * adds a new client handler to the list of waiting clients and if the timer
     * has expired or the queue has reached the max number of players for a
     * match, starts a new match.
     *
     * @param clientHandler
     *            :the client handler that has to be added
     */
    @Override
    public synchronized void addClient(ClientHandler clientHandler) {
        if(!disconnectedClientInMatch()) {
            if (waitingClients.size() == NetworkConstants.MINPLAYERS - 1)
                startInitialTimer();
            waitingClients.add(clientHandler);
            executor.submit(clientHandler);
            if (waitingClients.size() == NetworkConstants.MAXPLAYERS) {
                timer.interrupt();
                createMatch();
            }
        } else {
            try {
                executor.submit(clientHandler);
                clientHandler.addCommandObserver(this);
                clientHandler.sendCommand(new AskForReconnectionCommand());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean disconnectedClientInMatch() {
        for (MatchHandler match : createdMatches) {
            if (match.hasDisconnectedPlayer()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if there's the minimum number of waiting client to
     * start a match.
     *
     * @return true if there are at least NetworkConstants.MINPLAYERS players
     */
    public synchronized boolean checkWaitingList() {
        return waitingClients.size() >= NetworkConstants.MINPLAYERS;
    }

    public synchronized void timerExpired() {
        if (checkWaitingList())
            createMatch();
    }

    private void startInitialTimer() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FileConstants.INITIAL_TIME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int timeMillis = 0;
        try {
            timeMillis = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Thread(new InitialTimer(this, timeMillis));
        timer.start();
    }

    private synchronized void createMatch() {
        List<ClientHandler> list = new ArrayList<ClientHandler>();
        for (ClientHandler c : waitingClients)
            list.add(c);

        MatchHandler matchH = new MatchHandler(list, this);

        futures.put(matchH, executor.submit(matchH));
        createdMatches.add(matchH);
        waitingClients = new ConcurrentLinkedDeque<ClientHandler>();
    }
    private void closeServer() {
        closeWaitingList();
        if (timer != null && timer.isAlive())
            timer.interrupt();
        try {
            socketListener.endListening();
            inKeyboard.close();
            socketListener.endListening();
            rmiListener.endListening();
            closeMatches();
            executor.shutdown();
        } catch (IOException e) {}
    }
    private void closeWaitingList() {
        if (!waitingClients.isEmpty())
            for (ClientHandler c : waitingClients)
                try {
                    c.closedByServer();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
    }

    private void closeMatches() {
        List<MatchHandler> matchToClose = new ArrayList<MatchHandler>();
        for (MatchHandler m : createdMatches)
            matchToClose.add(m);
        for (MatchHandler m : matchToClose)
            m.closeMatch();
    }
    @Override
    public void removeClient(ClientHandler clientHandler) {
        waitingClients.remove(clientHandler);
        if(!checkWaitingList() && timer != null)
            timer.interrupt();

    }

    @Override
    public void closeMatch(MatchHandler matchHandler) {
        futures.get(matchHandler).cancel(true);

        createdMatches.remove(matchHandler);
        matchHandler = null;
    }

    public void notifyReconnectionAnswer(ReconnectionAnswerCommand command, ClientHandler clientHandler) {
        String answer = command.getAnswer();

        if (answer.equals("y")) {
            String username = command.getUsername();
            String password = command.getPassword();
            ArrayList<MatchHandler> possibleMatches = getMatchesWithDisconnectedUsers();
            ArrayList<User> users = null;
            try {
                users = UsersCreator.getUsersFromFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            User u = hasUserSignedUpCorrectly(username, password, users);

            if (u != null) {
                for (MatchHandler match : possibleMatches) {
                    if(match.hasDisconnectedUser(u)){
                        match.reconnectClient(clientHandler, match.getDisconnectedUser(u));
                        break;
                    }
                }
            }
        } else {
            this.addClientToWaiting(clientHandler);
        }
    }
    private void addClientToWaiting(ClientHandler clientHandler) {
        if (waitingClients.size() == NetworkConstants.MINPLAYERS - 1)
            startInitialTimer();
        waitingClients.add(clientHandler);
        executor.submit(clientHandler); // useless
        if (waitingClients.size() == NetworkConstants.MAXPLAYERS) {
            timer.interrupt();

            createMatch();
        }
    }

    private User hasUserSignedUpCorrectly(String username, String password, ArrayList<User> users) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.correctPassword(password))
                return u;
        }
        return null;
    }

    private ArrayList<MatchHandler> getMatchesWithDisconnectedUsers() {
        ArrayList<MatchHandler> matches = new ArrayList<>();
        for (MatchHandler match : createdMatches) {
            if (match.hasDisconnectedPlayer())
                matches.add(match);
        }
        return matches;
    }
}
