package ikov.pdungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;

public class FightBoss implements Strategy {
	
	public boolean activate() {
		try{
			for(int i = 0; i < PDungeoneering.BOSS_IDS.length; i++){
				if (Npcs.getNearest(PDungeoneering.BOSS_IDS[i]) != null && !PDungeoneering.nulledBoss){
					return true;
				}
			}
		}catch(Exception e){}
		return false;
	}

	public void execute() {
		//System.out.println("Fighting Npcs.getNearest(PDungeoneering.BOSS_IDS)[i]");
		try{
			//if(Npcs.getNearest().length > 0)
			//	System.out.println("Nearest NPC: "+Npcs.getNearest()[0].getLocation());
			for(int i = 0; i < PDungeoneering.BOSS_IDS.length; i++){
				final int iReplica = i;
				if (Npcs.getNearest(PDungeoneering.BOSS_IDS)[i] != null){
					if(!Npcs.getNearest(PDungeoneering.BOSS_IDS)[i].isInCombat()){
						if(Npcs.getNearest(PDungeoneering.BOSS_IDS)[i] != null){
							Npcs.getNearest(PDungeoneering.BOSS_IDS)[i].interact(Npcs.Option.ATTACK);
							Time.sleep(new SleepCondition(){
								public boolean isValid() {
									if(Npcs.getNearest(PDungeoneering.BOSS_IDS)[iReplica] != null){
										if (Npcs.getNearest(PDungeoneering.BOSS_IDS)[iReplica].isInCombat())
											return true;
									}
									if (Npcs.getNearest(PDungeoneering.THOK_ID) != null){
										if(Npcs.getNearest(PDungeoneering.THOK_ID).length > 0)
											return true;
									}
									return false;
								}
							},3600);
						}
					}
					PDungeoneering.nulledBossCheck = 0;
					//System.out.println("Boss: "+i+" ("+PDungeoneering.BOSS_IDS[i]+ ") "+Npcs.getNearest(PDungeoneering.BOSS_IDS)[i].getLocation()+" animation: "+Npcs.getNearest(PDungeoneering.BOSS_IDS)[i].getAnimation() + "  Health: "+Npcs.getNearest(PDungeoneering.BOSS_IDS)[i].getHealth()+"%");
				}
			}
		}catch(Exception e){
			if(e.getMessage().contains("0")){
				//System.out.println("Possible nulled Npcs.getNearest(PDungeoneering.BOSS_IDS)[i]? ArrayIndex 0 error");
				PDungeoneering.nulledBossCheck ++;
			}
			Time.sleep(600);
		}
		if(PDungeoneering.nulledBossCheck > 10){
			PDungeoneering.nulledBoss = true;
			System.out.println("BOSS NULLED - QUITTING DUNGEON");
			//Menu.sendAction(315, 5832704, 493, 16035, 356, 1);//quit dung lose prestige
			Time.sleep(600);
		}
	}
}
