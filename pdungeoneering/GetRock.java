package ikov.pdungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.Walking;
import org.rev317.min.api.wrappers.Tile;

public class GetRock implements Strategy {

	public boolean activate() {
		return Players.getMyPlayer().getLocation().getY() > 9000 && !PDungeoneering.gotRock;
	}

	public void execute() {
		//System.out.println("Getting Rock");
		try{
		if(!PDungeoneering.equipped){
			//System.out.println("Inventory length: "+Inventory.getItems().length);
			if(Inventory.getItems().length > 2){
				if(Inventory.getItems()[2] != null){
					//System.out.println("[2] is in slot "+Inventory.getItems()[2].getSlot());
					if(Inventory.getItems()[2].getSlot() > 2)
						PDungeoneering.equipped = true;
				}
			}
			if(Inventory.getItems().length >= 0 && PDungeoneering.equipped == false){
				//System.out.println("Equipping items");
				for(int i=0; i < 4; i++){
					if(Inventory.getItems().length > i){
						if(Inventory.getItems()[i] != null){
							if(Inventory.getItems()[i].getSlot() <= 4){
								Menu.sendAction(454, Inventory.getItems()[i].getId()-1, i, 3214, 1434,5);
								//Time.sleep(200);
								}
						}
					}
				}
				Menu.sendAction(1500, 23494656, 488, 267, 1434, 5);//toggle quick pray
				Time.sleep(600);
			}
			Time.sleep(600);
		}
		if(Players.getMyPlayer().getLocation().getY() > 9820){
			Menu.sendAction(502, 1168906693, 69, 51, 5808, 4);//Obtain rock from wall
			Time.sleep(new SleepCondition(){
				public boolean isValid() {
					return Inventory.contains(PDungeoneering.ROCK_ID);
				}
			},6000);
		} else {
			if(Players.getMyPlayer().getLocation().getY() < 9803){
				Walking.walkTo(new Tile(2612,9810));
				Time.sleep(1800);
			} else {
				Walking.walkTo(PDungeoneering.ROCK_TILE);
				Time.sleep(new SleepCondition(){
					public boolean isValid() {
						return PDungeoneering.ROCK_TILE.distanceTo() < 2;
					}
				},3600);
			}
		}
		if(Inventory.contains(PDungeoneering.ROCK_ID))
			PDungeoneering.gotRock = true;
		}catch(Exception e){
			System.out.print(e);
		}
		Time.sleep(600);
	}

	/*public boolean canSeeSculpture(){
		for(SceneObject obj : SceneObjects.getAllSceneObjects()){
			if(obj.getId() == PDungeoneering.SCULPTURE_ID && obj.getLocation() == PDungeoneering.ROCK_TILE)
				return true;
		}
		return false;
	}*/

}
