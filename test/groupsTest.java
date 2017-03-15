/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.Group.GroupFormer;
import Agents.Group._MODEL_SETUP;
import Agents.ProposerAgent;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Configuration.ConfigManager;
import Common.Logging.cLogConsole;
import auresearch.FactoryHolder;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author d3vil401
 */
public class groupsTest {
    
    public groupsTest() 
    {
        
    }
    
    @Test
    public void testGroups() throws IOException
    {
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._configManager = new ConfigManager();
        FactoryHolder._logManager.initializeSession("session");
        FactoryHolder._logManager.setDebugMode(false);
        
        ArrayList<SolverAgent> _solvers = new ArrayList();
        for (int i = 0; i < 10; i++)
            _solvers.add(new SolverAgent());
        
        ProposerAgent _proposer = new ProposerAgent();
        _proposer._generateProblem();
        
        
        GroupFormer _former = new GroupFormer(_solvers, _MODEL_SETUP.MODEL_1A);
        _former.attemptSolve(0, _proposer.getChallengeProposed());
    }
}
