package ikov.pdungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;

public class SummonBoss implements Strategy {

	public boolean activate() {
		return Inventory.contains(Pdungeoneering.ROCK_ID)
				&& Pdungeoneering.gotOrb
				&& Players.getMyPlayer().getLocation().getY() > 9000;
	}

	public void execute() {
		//System.out.println("Summoning boss");
		if(Pdungeoneering.BOSS_TILE.distanceTo() < 7
				&& Inventory.contains(Pdungeoneering.ROCK_ID)){
			Menu.sendAction(502, 1133287219, 51, 46, 3634, 3);//Touch strange shrine
			Time.sleep(new SleepCondition(){
				public boolean isValid() {
					return !Inventory.contains(Pdungeoneering.ROCK_ID)
							&& !Inventory.contains(Pdungeoneering.ORB_ID);
				}
			},1200);
		}
			
	}

}
