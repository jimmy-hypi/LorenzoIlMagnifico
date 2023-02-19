package model.area;

import constant.FileConstants;
import model.PersonalBonusTile;
import model.effect.CouncilPrivilegeEffect;
import model.effect.InstantResourcesEffect;
import model.effect.MultipleEffect;
import model.effect.NoEffect;
import model.resource.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class initializes the board components by taking decoding information stored in files
 */
public class BoardInitializer {
    public static ArrayList<Resource> territoryBonuses() throws FileNotFoundException, IOException{
        ArrayList<Resource> territoryBonuses = new ArrayList<Resource>();

        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.BONUSES_TERRITORY_TOWER));
        String lineRead = reader.readLine();

        while(lineRead != null){
            territoryBonuses.add(ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(lineRead) - 1], Integer.parseInt(reader.readLine())));
            lineRead = reader.readLine();
        }

        reader.close();
        return territoryBonuses;
    }

    public static ArrayList<Resource> buildingBonuses() throws FileNotFoundException, IOException{
        ArrayList<Resource> buildingBonuses = new ArrayList<Resource>();

        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.BONUSES_BUILDING_TOWER));
        String lineRead = reader.readLine();

        while(lineRead != null){
            buildingBonuses.add(ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(lineRead) - 1], Integer.parseInt(reader.readLine())));
            lineRead = reader.readLine();
        }

        reader.close();
        return buildingBonuses;
    }

    public static ArrayList<Resource> characterBonuses() throws FileNotFoundException, IOException{
        ArrayList<Resource> characterBonuses = new ArrayList<Resource>();

        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.BONUSES_CHARACTER_TOWER));
        String lineRead = reader.readLine();

        while(lineRead != null){
            characterBonuses.add(ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(lineRead) - 1], Integer.parseInt(reader.readLine())));
            lineRead = reader.readLine();
        }

        reader.close();
        return characterBonuses;
    }

    public static ArrayList<Resource> ventureBonuses() throws FileNotFoundException, IOException{
        ArrayList<Resource> ventureBonuses = new ArrayList<Resource>();

        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.BONUSES_VENTURE_TOWER));
        String lineRead = reader.readLine();

        while(lineRead != null){
            ventureBonuses.add(ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(lineRead) - 1], Integer.parseInt(reader.readLine())));
            lineRead = reader.readLine();
        }

        reader.close();
        return ventureBonuses;
    }

    public static MultipleEffect councilPalaceBonuses() throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.BONUSES_COUNCIL_PALACE));
        String lineRead = reader.readLine();

        while(lineRead != null){
            if(Integer.parseInt(lineRead)== 8){
                CouncilPrivilegeEffect council = new CouncilPrivilegeEffect(Integer.parseInt(reader.readLine()));
                ResourceChest chest = new ResourceChest();
                Resource resource = ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(reader.readLine())-1],Integer.parseInt(reader.readLine()));
                chest.addResource(resource);
                reader.close();
                return new MultipleEffect(council,new InstantResourcesEffect(chest));
            } else {
                ResourceChest chest = new ResourceChest();
                Resource resource = ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(lineRead)-1],Integer.parseInt(reader.readLine()));
                chest.addResource(resource);
                if(Integer.parseInt(reader.readLine())==8){
                    CouncilPrivilegeEffect council = new CouncilPrivilegeEffect(Integer.parseInt(reader.readLine()));
                    reader.close();
                    return new MultipleEffect(new InstantResourcesEffect(chest),council);
                } else {
                    ResourceChest chest2 = new ResourceChest();
                    Resource resource2 = ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(reader.readLine())-1],Integer.parseInt(reader.readLine()));
                    chest.addResource(resource2);
                    reader.close();
                    return new MultipleEffect(new InstantResourcesEffect(chest), new InstantResourcesEffect(chest2));
                }
            }
        }

        reader.close();
        return new MultipleEffect(new NoEffect(),new NoEffect());

    }

    public static VictoryPoint[] churchBonuses() throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.CHURCH_BONUSES));

        int[] bonuses = new int[15];
        int i = 0;

        String lineRead = reader.readLine();

        while(lineRead !=null){
            bonuses[i] = Integer.parseInt(lineRead);
            lineRead = reader.readLine();
        }

        reader.close();

        VictoryPoint[] victory = new VictoryPoint[15];

        for(int j = 0; j<bonuses.length; j++){
            victory[j] = new VictoryPoint(bonuses[j]);
        }

        return victory;
    }

    public static ArrayList<Integer> playerBoardBonusesForTerritory() throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.TERRITORYCARD_PLAYER_BONUSES));
        ArrayList<Integer> bonusesForTerritory = new ArrayList<Integer>();
        String lineRead = reader.readLine();

        while (lineRead != null){
            bonusesForTerritory.add(Integer.parseInt(lineRead));
            lineRead = reader.readLine();
        }

        reader.close();
        return bonusesForTerritory;
    }

    public static ArrayList<Integer> playerBoardBonusesForCharacter() throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.CHARACTERCARD_PLAYER_BONUSES));
        ArrayList<Integer> bonusesForCharacter = new ArrayList<Integer>();
        String lineRead = reader.readLine();

        while (lineRead != null){
            bonusesForCharacter.add(Integer.parseInt(lineRead));
            lineRead = reader.readLine();
        }

        reader.close();
        return bonusesForCharacter;
    }

    public static ArrayList<Integer> playerBoardRequirementsForTerritory() throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.TERRITORYCARD_PLAYER_REQUIREMENTS));
        ArrayList<Integer> requirementsForTerritory = new ArrayList<Integer>();
        String lineRead = reader.readLine();

        while (lineRead != null){
            requirementsForTerritory.add(Integer.parseInt(lineRead));
            lineRead = reader.readLine();
        }

        reader.close();
        return requirementsForTerritory;
    }

    public static PersonalBonusTile[] createPersonalBonusTiles(int tilesNumber) throws NumberFormatException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.PERSONAL_BONUS_TILES));
        PersonalBonusTile[] tiles = new PersonalBonusTile[tilesNumber];
        InstantResourcesEffect productionEffect;
        InstantResourcesEffect harvestEffect;
        int index = 0;
        String lineRead = reader.readLine();

        while(lineRead != null){
            productionEffect = new InstantResourcesEffect(new ResourceChest(
                    Integer.parseInt(lineRead), Integer.parseInt(reader.readLine()),
                    Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()),
                    Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()),
                    Integer.parseInt(reader.readLine())));

            harvestEffect = new InstantResourcesEffect(new ResourceChest(
                    Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()),
                    Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()),
                    Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()),
                    Integer.parseInt(reader.readLine())));

            lineRead = reader.readLine();
            tiles[index] = new PersonalBonusTile(productionEffect, harvestEffect);
            index++;
        }

        reader.close();
        return tiles;
    }

    public static ResourceChest[] createPrivilegeResources(int resourcesNumber) throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(FileConstants.PRIVILEGE_RESOURCES));
        ResourceChest[] privilegeResources = new ResourceChest[resourcesNumber];
        int index = 0;
        String lineRead = reader.readLine();

        while(lineRead!=null){
            Resource rt1 = ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(lineRead)], Integer.parseInt(reader.readLine()));
            Resource rt2 = ResourceFactory.getResource(ResourceType.values()[Integer.parseInt(reader.readLine())], Integer.parseInt(reader.readLine()));
            ResourceChest resourceChest = new ResourceChest();
            resourceChest.addResource(rt1);
            resourceChest.addResource(rt2);
            privilegeResources[index] = resourceChest;
            index++;
            lineRead = reader.readLine();
        }

        return privilegeResources;
    }
}
