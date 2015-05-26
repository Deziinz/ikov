package ikov.pfiremaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

@ScriptManifest(author="Agrodon", category = Category.FIREMAKING, description="Let's burn down the world!", name="PFiremaker", servers={"ikov"}, version=1.0)
public class PFiremaker extends Script implements Paintable, MessageListener{
	private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();
	public static final String[] LOG_NAME = {"Logs", "Oak logs", "Willow logs", "Teak logs", "Maple logs", "Mahogany logs", "Yew logs", "Magic logs"};
	public static final int[] LOG_ID = {1512, 1522, 1520, 6334, 1518, 6333, 1516, 1514};
	public static final int[] LEVEL_REQUIRED = {1, 15, 30, 35, 45, 50, 60, 75};
	public static final String[] LOCATION = {"Market", "Varrock east", "Varrock west"};
	public static final Tile[] LOCATION_TILE = {new Tile(3235, 3428), new Tile(3271, 3428), new Tile(3207, 3428)};
	public static final int TINDERBOX_ID = 591;
	public static final int BANK_ID = 2213;
	public static int logChoice = 7;
	public static int locChoice = 0;
	public static boolean autoProgress = false;
	public static boolean startedBurning = false;
	public static boolean findTile = false;
	public static boolean stopScript = false;
	public static String state = "";
	private Timer runTime = new Timer();
	private final int startExp = Skill.FIREMAKING.getExperience();
	private int logsBurnt = 0;
	GUI gui;
	
	@Override
	public boolean onExecute() {
		gui = new GUI();
		gui.setVisible(true);
		while (gui.isVisible()) {
			sleep(100);
		}
		System.out.println("Burning "+LOG_NAME[logChoice]+" at "+LOCATION[locChoice]+ (autoProgress ? " - auto progressing" : ""));
		strategies.add(new Login());
		strategies.add(new Banking());
		strategies.add(new OpenBank());
		strategies.add(new ToLocation());
		strategies.add(new Firemaking());
		provide(strategies);
		return true;
	}
	@Override
	public void onFinish() {
	}

	public static int findBankSlot(int itemID){
		int[] bankItemIDs = Loader.getClient().getInterfaceCache()[5382].getItems();
		for(int i = 0; i < bankItemIDs.length; i++){
			//System.out.println("#"+i+": "+bankItemIDs[i]);
			if(bankItemIDs[i] == itemID)
				return i;
		}
		return -1;
	}
	 
	@Override
	public void messageReceived(MessageEvent m) {
		String msg = m.getMessage();
		//System.out.println(msg);
		if(m.getType() == 0){
			if(msg.contains("does not exist") 
					|| msg.contains("already on your")
					|| msg.contains("Unable to receive input")
					|| msg.contains("player is not in your clan")
					|| msg.contains("not in a clan")
					|| msg.contains("wilderness to attack")){
				System.out.println(msg);
				System.out.println("Expecting client to be out of sync, relogging.");
				forceLogout();
			}
			if(msg.contains("to light that log")){
				System.out.println(msg);
				setState(STATE_STOPPED);
			}
			if(msg.contains("cannot light a fire"))
				findTile = true;
			if(msg.contains("fire catches"))
				logsBurnt++;
		}
	}

	//Big thanks to Minimal for the forceLogout method.
	public static void forceLogout(){
		try {
			Class<?> c = Loader.getClient().getClass();
			Method m = c.getDeclaredMethod("am");
			m.setAccessible(true);
			m.invoke(Loader.getClient());
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static Tile validTile(){
		int myX = Players.getMyPlayer().getLocation().getX();
		int myY = Players.getMyPlayer().getLocation().getY();
		for( int i = 0; i < 5; i++ ){
			for (int j = 0; j < 5; j++){
				if(!objectOnTile(new Tile(myX-i, myY+j)))
					return new Tile(myX-i, myY+j);
			}
		}
		return null;
	}
	
	public static boolean objectOnTile(Tile t){
		for(SceneObject obj:SceneObjects.getNearest()){
			if(obj.getLocation().getX() == t.getLocation().getX()
					&& obj.getLocation().getY() == t.getLocation().getY())
				return true;
		}
		return false;
	}

	public static boolean onTile(Tile first, Tile second){
		return first.getX() == second.getX()
				&& first.getY() == second.getY();
				//&& first.getPlane() == second.getPlane();
	}
	
	public static boolean inArea(int x, int y, int x2, int y2){
		return Players.getMyPlayer().getLocation().getX() <= x2
				&& Players.getMyPlayer().getLocation().getX() >= x
				&& Players.getMyPlayer().getLocation().getY() <= y2
				&& Players.getMyPlayer().getLocation().getY() >= y;
	}
	
 	@Override
 	public void paint(Graphics arg0) {
 		Graphics2D g = (Graphics2D)arg0;
 		Color white = Color.white;
 		Color black = Color.black;
 		int y = 230;
 		int expGained = Skill.FIREMAKING.getExperience() - startExp;
 		int expHr = runTime.getPerHour(expGained);
 		shadowedString(g, "Runtime: "+runTime.toString(), 560, y, white, black);
 		shadowedString(g, "State: ", 560, y+25, white, black);
 		shadowedString(g, state, 630, y+25, white, black);
 		shadowedString(g, "Fm exp (/hr):", 560, y+45, white, black);
 		shadowedString(g, formatBigNumber(expGained)+" ("+formatBigNumber(expHr)+")", 650, y+45, white, black);
 		shadowedString(g, "Logs burnt (/hr):", 560, y+60, white, black);
 		shadowedString(g, formatBigNumber(logsBurnt)+" ("+formatBigNumber(runTime.getPerHour(logsBurnt))+")", 650, y+60, white, black);
 		
 		shadowedString(g, "PFiremaker", 655, 463, white, black);
 	}
 	
 	public void shadowedString(Graphics g, String text, int x, int y, Color front, Color back){
 		g.setColor(back);
 		g.drawString(text, x+1, y-1);
 		g.setColor(front);
 		g.drawString(text, x, y);
 	}

 	public String formatBigNumber(int num){
 		return (String) (num > 100000000 ? num/1000000+"m" : (num > 100000 ? num/1000+"k" : ""+num));
 	}
 	
 	public class Login implements Strategy {
 		
 		@Override
 		public boolean activate() {
 			return !Game.isLoggedIn() || stopScript;
 		}

 		@Override
 		public void execute() {
 			if(stopScript)
 				setState(STATE_STOPPED);
 			else{
	 			PFiremaker.state = "Logging in";
	 			Mouse.getInstance().click(new Point(380,315));
	 			Time.sleep(5000);
 			}
 		}
 	}
}