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
	
	private KnowledgeBuilder kbuilder;
	private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;
    private Vector<FactHandle> referenciasHechosActuales = new Vector<FactHandle>();
    
    public CheckRulesCorleone() {
    	String debugMode = System.getProperty("robot.debug", "true");
    	DEBUG.enableDebugMode(debugMode.equals("true"));
    	createKnowledgeBase();
    	loadEvents();
    }
    
    private void loadEvents() {
    	
    }
    
    private void createKnowledgeBase() {
    	
    }
    
    public static void main(String args[]) {
    	CheckRulesCorleone rules = new CheckRulesCorleone();
    }
    
    private List<DonCorleoneAction> recoverActions() {
    	DonCorleoneAction action;
    	Vector<DonCorleoneAction> actionsList = new Vector<DonCorleoneAction>();
    	
    	for(QueryResultsRow result : ksession.getQueryResults(DonCorleone.CONSULT_ACTIONS)) {
    		action = (DonCorleoneAction) result.get("action");
    		action.setRobot(null);
    		actionsList.add(action);
    		ksession.retract(result.getFactHandle("action"));
    	}
    	
    	return actionsList;
    }
}
