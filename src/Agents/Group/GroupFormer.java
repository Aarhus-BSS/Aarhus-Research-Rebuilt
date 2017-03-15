/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

import Agents.Group.Group;
import Agents.Group._MODEL_SETUP;
import Agents.Properties.cSkill;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import Environment.cRound;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 *
 * @author d3vil401
 */
public class GroupFormer 
{
    private ArrayList<Group> _groupPool = new ArrayList();
    private ArrayList<SolverAgent> _sAPool = new ArrayList();
    private _MODEL_SETUP _requestedMethod = _MODEL_SETUP.MODEL_1A;
    static int _skillOrderIndex = 0;

    public GroupFormer(ArrayList<SolverAgent> _sAgents, _MODEL_SETUP _model) 
    {
        this._createGroups(_sAgents, _model);
    }

    public GroupFormer(ArrayList<SolverAgent> _sAgents) 
    {
        this._createGroups(_sAgents, _MODEL_SETUP.MODEL_1A);
    }

    private boolean _canProceedWithChallenge(SolverAgent _agent, Challenge _skillMap) 
    {
        for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++k) 
            if (_agent.getSkills().get(k).getExperience() < _skillMap.getDifficultyMap()[k]) 
                return false;
            
        return _agent.getTryHarded() <= FactoryHolder._configManager.getNumberValue("NUMBER_OF_TRIALS_FOR_SINGLE_AGENT_SOLVING");
    }
    
    private static double sigmoid(double x) 
    {
        return (1 / (1 + Math.pow(Math.E, (-1 * x))));
    }
    
    static final Comparator<SolverAgent> SKILLORDER = 
                                        new Comparator<SolverAgent>() 
                                        {
                                            @Override
                                            public int compare(SolverAgent e1, SolverAgent e2) 
                                            {
                                                return (e1.getSkills().get(_skillOrderIndex).getExperience() - e2.getSkills().get(_skillOrderIndex).getExperience());
                                            }
                                        };

    private void _createGroups(ArrayList<SolverAgent> _sAgents, _MODEL_SETUP _model) {
        this._requestedMethod = _model;
        this._sAPool = _sAgents;
        
        switch (this._requestedMethod) 
        {
            case MODEL_1A: 
            
                // It's going from minimum to maximum, then reverse it.
                Collections.sort(this._sAPool, SKILLORDER);
                Collections.reverse(this._sAPool);
                
                break;
            
            case MODEL_1B: 
            
                break;
            
            case MODEL_1A_WR:
                
                break;
                
            case MODEL_1B_WR:
                
                break;
            default: 
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Incorrect model specified in group formation process.");
        }
    }
    
    public void _formGroupsTorwardChallenge(Challenge _challenge)
    {
        ArrayList<SolverAgent> _canAlone = new ArrayList();
        ArrayList<SolverAgent> _mustInGroup = new ArrayList();
        
        for (int i = 0; i < this._sAPool.size(); i++)
            if (cRound._canProceedWithChallenge(this._sAPool.get(i), _challenge))
                _canAlone.add(this._sAPool.get(i));
            else
                _mustInGroup.add(this._sAPool.get(i));
        
        
    }
    
    private double[] _extractRequirements(Challenge _challenge)
    {
        double _requirements[] = new double[_challenge.getDifficultyMap().length];
        int _reqAmount = 0;
        
        for (int i = 0; i < _challenge.getDifficultyMap().length; i++)
            if (_challenge.getDifficultyMap()[i] > 0)
                _requirements[_reqAmount++] = _challenge.getDifficultyMap()[i];
        
        double _filteredRequirements[] = new double[_reqAmount];
        
        for (int i = 0; i < _reqAmount; i++)
            _filteredRequirements[i] = _requirements[i];
        
        return _filteredRequirements;
    }
    
    public boolean attemptSolve(int _groupIndex, Challenge _challenge)
    {
        if (!this._groupPool.isEmpty())
        {
            if (FactoryHolder._configManager.getStringValue("USE_SIGMOID").equals("true"))
            {
                double _saturationPoints[] = new double[this._extractRequirements(_challenge).length];
                for (int i = 0; i < _saturationPoints.length; i++)
                    _saturationPoints[i] = 0;
                double _skillAvgMap[] = new double[_saturationPoints.length];
                
                // Make avg of the same skill for each group member
                //for (int i = 0; i < this._groupPool.get(_groupIndex).getMembers().length; i++)
                //    for (int skill = 0; skill < _skillAvgMap.length; skill++)
                //        _skillAvgMap[skill] += this._groupPool.get(_groupIndex).getMembers()[i].getSkills().get(skill).getExperience();
                
                for (int i = 0; i < _skillAvgMap.length; i++)
                    _skillAvgMap[i] /= _skillAvgMap.length;
                
                
                
            } else {
                    
            }
        } else
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "No groups, can't try to solve anything here.");
        
        return false;
    }

    public ArrayList<Group> getFormedGroups() 
    {
        return this._groupPool;
    }

    public long getGroupCount() 
    {
        return this._groupPool.size();
    }
}
