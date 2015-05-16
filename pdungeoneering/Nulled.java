package ikov.pdungeoneering;

import java.awt.Point;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;

public class Nulled implements Strategy {

	public boolean activate() {
		return PDungeoneering.nulledBoss 
				|| !Game.isLoggedIn();
	}

	public void execute() {
		if(!Game.isLoggedIn()){
			Mouse.getInstance().click(new Point(380,315));
			PDungeoneering.nulledBoss = false;
			PDungeoneering.nulledBossCheck = 0;
		} else if(!Players.getMyPlayer().isInCombat()){
			System.out.println("Logging out normally.");
			Menu.sendAction(315, 1353, 498, 2458, 48613, 1);//logout button
		} else if ( Players.getMyPlayer().getLocation().getY() > 9000){
			System.out.println("Forcefully logging out.");
			PDungeoneering.forceLogout();
		}
		Time.sleep(3000);
	}


}
