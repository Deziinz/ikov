package ikov.pdungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;

public class StartDungeon implements Strategy {

	public boolean activate() {
		try{
		return Npcs.getNearest(PDungeoneering.THOK_ID).length > 0 || Game.getOpenBackDialogId() == 2469 ;
		}catch(Exception e){return false;}
	}

	public void execute() {
		//System.out.println("Starting dungeon");
		PDungeoneering.gotRock = false;
		PDungeoneering.gotOrb = false;
		PDungeoneering.equipped = false;
		PDungeoneering.nulledBossCheck = 0;
		if(Game.getOpenBackDialogId() == 2469){
			Menu.sendAction(315, 794558464, 55, 2472, 48496, 1);//Second option dung chatbox
			Time.sleep(new SleepCondition(){
				public boolean isValid() {
					try{
					return Npcs.getNearest(PDungeoneering.THOK_ID).length == 0;
					}catch(Exception e){return false;}
				}
			},2400);
			if(Players.getMyPlayer().getLocation().getY() > 9000)
				Time.sleep(8400);
		} else {
			Menu.sendAction(225, 1273, 0, 0, 48496, 4);//open start dung interface
			Time.sleep(new SleepCondition(){
				public boolean isValid() {
					return Game.getOpenBackDialogId() == 2469;
				}
			},6000);
		}
		PDungeoneering.dungTimer = System.currentTimeMillis();
	}
}
