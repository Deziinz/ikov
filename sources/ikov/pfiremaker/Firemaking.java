package ikov.pfiremaker;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Items;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.Walking;
import org.rev317.min.api.wrappers.Tile;

public class Firemaking implements Strategy {

	@Override
	public boolean activate() {
		return Inventory.contains(PFiremaker.LOG_ID[PFiremaker.logChoice]) && Inventory.contains(PFiremaker.TINDERBOX_ID);
	}

	@Override
	public void execute() {
		if(PFiremaker.findTile){
			PFiremaker.state = "Finding tile";
			final Tile t = PFiremaker.validTile();
			if(t != null){
				Walking.walkTo(PFiremaker.validTile());
				Time.sleep(new SleepCondition(){
					@Override
					public boolean isValid() {
						if(t.distanceTo() == 0){
								PFiremaker.findTile = false;
							return true;
						} return false;
					}
				},3000);
			}
		}
		PFiremaker.startedBurning = true;
		PFiremaker.state = "Setting fires";
		final Tile currLoc = Players.getMyPlayer().getLocation();
		Inventory.getItem(PFiremaker.TINDERBOX_ID).interact(Items.Option.USE);
		Inventory.getItem(PFiremaker.LOG_ID[PFiremaker.logChoice]).interact(Items.Option.USE_WITH);
		Time.sleep(new SleepCondition(){
			@Override
			public boolean isValid() {
				return !PFiremaker.onTile(currLoc, Players.getMyPlayer().getLocation());
			}
		},3000);
		Time.sleep(300);
	}
}
