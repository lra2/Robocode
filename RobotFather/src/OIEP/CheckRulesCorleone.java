package OIEP;

import java.util.List;
import java.util.Vector;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResultsRow;

import robocode.*;

public class CheckRulesCorleone {
	public static String RULES_FILE = "RobotFather/rules/DonCorleoneRules.drl";
	public static String CONSULT_ACTIONS = "consult_actions";
	
	private KnowledgeBuilder builder;
	private KnowledgeBase base;
    private StatefulKnowledgeSession session;
    private Vector<FactHandle> currentReferencedFacts = new Vector<FactHandle>();
    
    public CheckRulesCorleone() {
    	String debugMode = System.getProperty("robot.debug", "true");
    	DEBUG.enableDebugMode(debugMode.equals("true"));
    	createKnowledgeBase();
    	loadEvents();
    }
    
    private void loadEvents() {
		ScannedRobotEvent ev = new ScannedRobotEvent("pepe", 100, 10, 10, 10, 10);
		FactHandle referenceMade = session.insert(ev);
		currentReferencedFacts.add(referenceMade);

		DEBUG.message("Facts in active memory.");
		DEBUG.dumpFacts(session);
		session.fireAllRules();
		List<DonCorleoneAction> actions = recoverActions();
		DEBUG.message("Resulting actions.");
		DEBUG.dumpActions(actions);
    }
    
    private void createKnowledgeBase() {
		String rulesFile;
		rulesFile = System.getProperty("robot.rules", CheckRulesCorleone.RULES_FILE);

		DEBUG.message("Create knowledge base.");
		builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		DEBUG.message("Load rules from " + rulesFile);
		builder.add(ResourceFactory.newClassPathResource(rulesFile, CheckRulesCorleone.class), ResourceType.DRL);
		if(builder.hasErrors()) {
			System.err.println(builder.getErrors().toString());
		}

		base = KnowledgeBaseFactory.newKnowledgeBase();
		base.addKnowledgePackages(builder.getKnowledgePackages());

		DEBUG.message("Create session (active memory)");
		session = base.newStatefulKnowledgeSession();
    }
    
    public static void main(String args[]) {
    	CheckRulesCorleone rules = new CheckRulesCorleone();
    }
    
    private List<DonCorleoneAction> recoverActions() {
    	DonCorleoneAction action;
    	Vector<DonCorleoneAction> actionsList = new Vector<DonCorleoneAction>();
    	
    	for(QueryResultsRow result : session.getQueryResults(DonCorleone.CONSULT_ACTIONS)) {
    		action = (DonCorleoneAction) result.get("action");
    		action.setRobot(null);
    		actionsList.add(action);
    		session.retract(result.getFactHandle("action"));
    	}
    	
    	return actionsList;
    }
}
