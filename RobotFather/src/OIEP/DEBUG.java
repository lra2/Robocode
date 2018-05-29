package OIEP;

import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

public final class DEBUG {
	public static boolean debugModeEnabled = false;
	
	public static void enableDebugMode(boolean flag) {
		debugModeEnabled = flag;
	}
	
	public static void message(String message) {
		if(debugModeEnabled) {
			System.out.println("DEBUG:" + message);
		}
	}
	
	public static void dumpFacts(StatefulKnowledgeSession session) {
		if(debugModeEnabled) {
			for (FactHandle f: session.getFactHandles()){
				System.out.println("  " + session.getObject(f));				
			}
		}
	}
	
	public static void dumpActions(List<DonCorleoneAction> actions) {
		if(debugModeEnabled) {
			for(DonCorleoneAction a: actions) {
				System.out.println("  " + a.toString());
			}
		}
	}
}
