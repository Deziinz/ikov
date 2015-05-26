package ikov.pfiremaker;


import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Skill;

public class Banking implements Strategy {

	@Override
	public boolean activate() {
		return (!Inventory.contains(PFiremaker.LOG_ID[PFiremaker.logChoice]) || !Inventory.contains(PFiremaker.TINDERBOX_ID))
				&& Game.getOpenInterfaceId() == 5292;
	}

	@Override
	public void execute() {
		PFiremaker.state = "Banking";
		PFiremaker.startedBurning = false;
		if(PFiremaker.autoProgress){
			for(int i = PFiremaker.LEVEL_REQUIRED.length-1; i >= 0 ; i--){
				if(Skill.FIREMAKING.getRealLevel() >= PFiremaker.LEVEL_REQUIRED[i] 
						&& PFiremaker.findBankSlot(PFiremaker.LOG_ID[i]) != -1){
					PFiremaker.logChoice = i;
					System.out.println("New log: "+PFiremaker.LOG_NAME[PFiremaker.logChoice]);
					break;
				}
			}
		}
		if(PFiremaker.findBankSlot(PFiremaker.LOG_ID[PFiremaker.logChoice]) == -1){
			System.out.println("Out of logs, stopping script");
			PFiremaker.stopScript = true;
		}
		if(Inventory.getCount() > 1 || !Inventory.contains(PFiremaker.TINDERBOX_ID))
			Bank.depositAllExcept(PFiremaker.TINDERBOX_ID);
		if(!Inventory.contains(PFiremaker.TINDERBOX_ID))
			Menu.sendAction(632, PFiremaker.TINDERBOX_ID-1, PFiremaker.findBankSlot(PFiremaker.TINDERBOX_ID), 5382, 2213, 8);
		if(!Inventory.contains(PFiremaker.LOG_ID[PFiremaker.logChoice]))
			Menu.sendAction(431, PFiremaker.LOG_ID[PFiremaker.logChoice]-1, PFiremaker.findBankSlot(PFiremaker.LOG_ID[PFiremaker.logChoice]), 5382, 2213, 4);
		Time.sleep(new SleepCondition(){
			@Override
			public boolean isValid() {
				return Inventory.contains(PFiremaker.LOG_ID[PFiremaker.logChoice]) && Inventory.contains(PFiremaker.TINDERBOX_ID);
			}
		},2000);
		PFiremaker.state = "idle";
		Time.sleep(200);
	}

}
