package model.excommunicationtile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import constant.FileConstants;
import exception.IllegalCardTypeException;
import model.Period;
import model.card.CardType;
import model.effect.Effect;
import model.effect.HarvestBonusEffect;
import model.effect.ProductionBonusEffect;
import model.effect.RaiseValueWithDiscountEffect;
import model.resource.Resource;
import model.resource.ResourceFactory;
import model.resource.ResourceType;
import model.resource.VictoryPoint;

/**
 * The class that creates the excommunication tiles from file
 */
public class ExcommunicationTilesCreator {
	private static BufferedReader buffReader;
	private static String lineRead;

	public static ExcommunicationTile[] createExcommunicationTiles(int tiles) throws IOException{
		Period period; 
		String s;
		int index=0;
		Resource r1=null;
		Resource r2=null;
		Effect effect;
		int rt1;
		int rt2;
		int n1;
		int t;
		int n2;
		CardType cardType = null;
		ExcommunicationTile[] tilesArray=new ExcommunicationTile[tiles];
		
		buffReader = new BufferedReader(new FileReader(FileConstants.EXCOMMUNICATIONTILES));
		lineRead = buffReader.readLine();  //line 1  	
		while (lineRead!=null) {
			r1=null;
			n1=Integer.parseInt(lineRead);   
			rt1=Integer.parseInt(buffReader.readLine()); //line 2
			if(rt1!=0)
			r1=ResourceFactory.getResource(ResourceType.values()[rt1-1],n1);
			
			n1=Integer.parseInt(buffReader.readLine()); //line 3
			rt1=Integer.parseInt(buffReader.readLine());  //line 4
			if(rt1!=0)
			r2=ResourceFactory.getResource(ResourceType.values()[rt1-1],n1);
			
			lineRead=buffReader.readLine();
			period=Period.values()[Integer.parseInt(lineRead)-1];  //line 5
			if(r1==null){
				n1=Integer.parseInt(buffReader.readLine());  //line 6
				if(n1==0){
					n1=Integer.parseInt(buffReader.readLine()); //line 7
					if(n1==0){
						n1=Integer.parseInt(buffReader.readLine()); //line 8
						if(n1==0){
							n1=Integer.parseInt(buffReader.readLine()); //line 9
							t=Integer.parseInt(buffReader.readLine()); //line 10
							if(t!=0){
							try{
							cardType=CardType.values()[t-1];
							}catch(Exception e){
								throw new IllegalCardTypeException();
							}
							}
							if(n1==0){
								n1=Integer.parseInt(buffReader.readLine()); //line 11
								if(n1==0){
									n1=Integer.parseInt(buffReader.readLine()); //line 12
									if(n1==0){
										n1=Integer.parseInt(buffReader.readLine()); //line 13
										if(n1==0){
											t=Integer.parseInt(buffReader.readLine()); //line 14
											if(t!=0){
												try{
												cardType=CardType.values()[t-1];
												}catch(Exception e){
													throw new IllegalCardTypeException();
												}
												}
											if(t==0){
												n1=Integer.parseInt(buffReader.readLine()); //line 15
												n2=Integer.parseInt(buffReader.readLine()); //line 16
												rt1=Integer.parseInt(buffReader.readLine()); //line 17
												if(n1==0){
													t=Integer.parseInt(buffReader.readLine()); //line 18
													if(t!=0){
														try{
														cardType=CardType.values()[t-1];
														}catch(Exception e){
															throw new IllegalCardTypeException();
														}
														}
													if(t==0){
														n1=Integer.parseInt(buffReader.readLine()); //line 19
														
														effect=new LosePointsForEveryResourceEffect(new VictoryPoint(n1));
														tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
		
													}
													else {
														if(t!=0){
															try{
															cardType=CardType.values()[t-1];
															}catch(Exception e){
																throw new IllegalCardTypeException();
															}
															
														effect=new LosePointsEveryWoodStoneEffect(new VictoryPoint(1), cardType);
														tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
														}
														
														s=buffReader.readLine();  //line 19
													}
												}
												else {
													//LosePointsBasedOnResourcesEffect
													VictoryPoint vp;
													r1=ResourceFactory.getResource(ResourceType.values()[6-1],n1);
													vp=(VictoryPoint)r1;
													r2=ResourceFactory.getResource(ResourceType.values()[rt1-1],n2);
													
													effect=new LosePointsBasedOnResourcesEffect(r2, vp);
													tilesArray[index]=new ExcommunicationTile(period, effect,index+1);

													for(int i=18;i<20;i++)s=buffReader.readLine();  //lines 18 to 19
												}
											}
											else {
												//no final points for cartType type card (can't be building type)
												effect=new SetNoCardTypeFinalPointsEffect(cardType);
												tilesArray[index]=new ExcommunicationTile(period, effect,index+1);

												for(int i=15;i<20;i++)s=buffReader.readLine();  //lines 15 to 19
											}
										}
										else {
											effect=new SetSkipRoundEffect();
											tilesArray[index]=new ExcommunicationTile(period, effect,index+1);

											
											for(int i=14;i<20;i++)s=buffReader.readLine();  //lines 14 to 19
										}
									}
									else {
										effect=new SetServantsDividerEffect(n1);
										tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
										
										for(int i=13;i<20;i++)s=buffReader.readLine();  //lines 13 to 19
									}
								}
								else {
									effect=new SetNoMarketActionEffect();
									tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
									
									for(int i=12;i<20;i++)s=buffReader.readLine();  //lines 12 to 19
								}
							}
							else {
								effect=new RaiseValueWithDiscountEffect(n1, cardType, false, false);
								tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
								for(int i=11;i<20;i++)s=buffReader.readLine();  //lines 11 to 19
							}
						}
						else {
							effect = new ColoredFamiliarsVariationEffect(-n1);
							tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
							for(int i=9;i<20;i++)s=buffReader.readLine();  //lines 9 to 19
						}	
					}
					else {
						effect = new ProductionBonusEffect(-n1);
						tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
						for(int i=8;i<20;i++)s=buffReader.readLine();  //lines 8 to 19
					}	
				}
				else{ 
					effect = new HarvestBonusEffect(-n1);
						tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
					
					for(int i=7;i<20;i++)s=buffReader.readLine();  //lines 7 to 19
				}
			}
			else{
				ArrayList<Resource> resource = new ArrayList<Resource>();
					resource.add(r1);
					if(r2!=null){
					resource.add(r2);
					}
					effect = new ResourceMalusEffect(resource);
					tilesArray[index]=new ExcommunicationTile(period, effect,index+1);
				
				for(int i=6;i<20;i++)s=buffReader.readLine();   //lines 6 to 19
			}
			index++;
			lineRead = buffReader.readLine();
		}

		return tilesArray;
	}
}