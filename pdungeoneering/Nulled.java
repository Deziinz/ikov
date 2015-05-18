package ikov.pdungeoneering;

import java.awt.Point;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Players;

public class Nulled implements Strategy {

	public boolean activate() {
		return PDungeoneering.nulledBoss 
				|| !Game.isLoggedIn()
				|| (System.currentTimeMillis() - PDungeoneering.dungTimer > 300000 
						&& Players.getMyPlayer().getLocation().getY() > 9000);
	}

	public void execute() {
		if(!Game.isLoggedIn()){
			Mouse.getInstance().click(new Point(380,315));
			PDungeoneering.nulledBoss = false;
			PDungeoneering.nulledBossCheck = 0;
		} else if ( Players.getMyPlayer().getLocation().getY() > 9000){
			System.out.println("Forcefully logging out.");
			PDungeoneering.forceLogout();
		}
		Time.sleep(5000);
	}


}
