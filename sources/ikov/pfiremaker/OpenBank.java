package ikov.pfiremaker;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

public class OpenBank implements Strategy {

	@Override
	public boolean activate() {
		return Game.getOpenInterfaceId() != 5292
				&& !Inventory.contains(PFiremaker.LOG_ID[PFiremaker.logChoice]);
	}

	@Override
	public void execute() {
		PFiremaker.state = "Opening bank";
		if(SceneObjects.getNearest(PFiremaker.BANK_ID).length > 0){
			if(SceneObjects.getNearest(PFiremaker.BANK_ID)[0] != null){
				SceneObject bank = SceneObjects.getNearest(PFiremaker.BANK_ID)[0];
				bank.interact(SceneObjects.Option.USE);
				Time.sleep(	new SleepCondition(){
					public boolean isValid(){
							return Game.getOpenInterfaceId() == 5292;
						}
				},4800);
			}
		} else {
			if(PFiremaker.inArea(3150, 3400, 3300, 3450) 
					&& (PFiremaker.LOCATION[PFiremaker.locChoice] == "Market"
						|| PFiremaker.LOCATION[PFiremaker.locChoice] == "Varrock east"
						|| PFiremaker.LOCATION[PFiremaker.locChoice] == "Varrock west"
				)){
				PFiremaker.state = "Teleporting to bank";
				Keyboard.getInstance().sendKeys("::market");
				Time.sleep(new SleepCondition(){
					@Override
					public boolean isValid() {
						return PFiremaker.onTile(Players.getMyPlayer().getLocation(),new Tile(3212, 3424));
					}
				},6000);
				Time.sleep(1200);
			}
		}
		PFiremaker.state = "idle";
		Time.sleep(200);
	}
}
