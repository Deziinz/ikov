package ikov.pfiremaker;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.Walking;
import org.rev317.min.api.wrappers.Tile;

public class ToLocation implements Strategy {

	@Override
	public boolean activate() {
		return Inventory.contains(PFiremaker.LOG_ID[PFiremaker.logChoice]) 
				&& Inventory.contains(PFiremaker.TINDERBOX_ID)
				&& PFiremaker.LOCATION_TILE[PFiremaker.locChoice].distanceTo() > 3
				&& (!PFiremaker.startedBurning || !PFiremaker.inArea(3150, 3400, 3300, 3450));
		}

	@Override
	public void execute() {
		PFiremaker.state = "Walking to location";
		System.out.println("Walking to tile");
		PFiremaker.findTile = true;
		if(PFiremaker.LOCATION_TILE[PFiremaker.locChoice].distanceTo() < 30){
			Walking.walkTo(PFiremaker.LOCATION_TILE[PFiremaker.locChoice]);
			Time.sleep(new SleepCondition(){
				@Override
				public boolean isValid() {
					return PFiremaker.LOCATION_TILE[PFiremaker.locChoice].distanceTo() == 0;
				}
			},6000);
			Time.sleep(200);
		} else {
			if(PFiremaker.LOCATION[PFiremaker.locChoice] == "Varrock west" || !PFiremaker.inArea(3150, 3400, 3300, 3450)){	
				PFiremaker.state = "Teleporting to market";
				Keyboard.getInstance().sendKeys("::market");
				Time.sleep(new SleepCondition(){
					@Override
					public boolean isValid() {
						return PFiremaker.onTile(Players.getMyPlayer().getLocation(),new Tile(3212, 3424));
					}
				},6000);
				Time.sleep(1200);
			} else {
				Walking.walkTo(Walking.getNearestTileTo(PFiremaker.LOCATION_TILE[PFiremaker.locChoice]));
				Time.sleep(2400);
			}
		}
		PFiremaker.state = "idle";
		Time.sleep(200);
	}

}
