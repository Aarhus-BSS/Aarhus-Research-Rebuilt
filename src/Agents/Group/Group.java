/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import Common.Math.SigmoidedThrows;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author d3vil401
 */
public class Group 
{
    private ArrayList<SolverAgent> _groupMembers = new ArrayList<>();
    private Random _random = new Random();
    private Challenge _solvedChallenge = null;
    private Challenge _formedFor = null;
    private boolean _coveredRequirements = false;
    private boolean[] _coveredMap = null;
    
    public Group(SolverAgent _a, SolverAgent _b)
    {
        this._groupMembers.add(_a);
        this._groupMembers.add(_b);
    }
    
    public Group(ArrayList<SolverAgent> _solvers)
    {
        for (SolverAgent i: _solvers)
            this._groupMembers.add(i);
    }
    
    private int[] _extractedSkills(SolverAgent _sa, int _requirementsAmount)
    {
        int[] _skillMap = new int[_requirementsAmount];
        for (int i = 0; i < _requirementsAmount; i++)
            _skillMap[i] = _sa.getSkills().get(i).getExperience();
        
        return _skillMap;
    }
    
    private int _countCoveredRequirements(SolverAgent _agent)
    {
        for (int i = 0; i < this._formedFor.getDifficultyMap().length - 1; i++)
            if (_agent.getSkills().get(i).getExperience() >= this._formedFor.getDifficultyMap()[i])
                this._coveredMap[i] = true;
        
        int _mapCount = 0;
        for (int i = 0; i < this._coveredMap.length; i++)
            if (this._coveredMap[i] == true)
                _mapCount++;
        
        this._coveredRequirements = _mapCount == this._formedFor.getDifficultyMap().length;
        
        return 0;
    }
    
    private int _requirementIndex = 0;
    
    public boolean _satured()
    {
        if (this._groupMembers.size() < FactoryHolder._configManager.getNumberValue("MINIMUM_MEMBERS_TO_ATTEMPT_SOLVE"))
            return true;
        
        return false;
    }
    
    public Group(ArrayList<SolverAgent> _solvers, Challenge _challenge, _MODEL_SETUP _model)
    {
        this._formedFor = _challenge;
        this._requirementIndex = 0;
        int[] _actualRequirements = this._formedFor.getDifficultyMap();
        this._coveredMap = new boolean[this._formedFor.getDifficultyMap().length];
        
        switch (_model)
        {
            case MODEL_1A:
                for (int i = 0; i < _solvers.size(); i++)
                {
                    if ( _requirementIndex <= (FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size() - 1))
                    {
                        if (this._coveredMap[_requirementIndex] != true)
                        {
                            int[] _agentSkills = this._extractedSkills(_solvers.get(i), this._formedFor.getDifficultyMap().length);

                            if (_agentSkills[_requirementIndex] >= _actualRequirements[_requirementIndex]
                                && _solvers.get(i)._isInGroup != true
                                && _solvers.get(i)._solvedLastChallengeAsGroup != true
                                && i != _solvers.size())
                            {
                                this._countCoveredRequirements(_solvers.get(i));

                                _solvers.get(i)._isInGroup = true;
                                this._groupMembers.add(_solvers.get(i));
                                this._requirementIndex++;
                            }
                        } else {
                            _requirementIndex++;
                        }
                    } else {
                        break;
                    }
                }
                break;
                
            case MODEL_1B:
                
                break;
                
            case MODEL_1A_WR:
                
                break;
            case MODEL_1B_WR:
                
                
                
                break;
            default:
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Unrecognized model " + _model.toString());
                return;
        }
        
        this._coveredRequirements = (this._requirementIndex + 1) == this._formedFor.getDifficultyMap().length;
    }
    
    public boolean isInGroup(SolverAgent _agent)
    {
        for (SolverAgent _groupMember : this._groupMembers) 
            if (_groupMember.equals(_agent)) 
                return true;
            
        return false;
    }
    
    public int getMembersCount()
    {
        return this._groupMembers.size();
    }
    
    public void disband()
    {
        for (int i = 0; i < this._groupMembers.size(); i++)
        {
            this._groupMembers.get(i)._isInGroup = false;
            //this._groupMembers.remove(i);
        }
        
        //this._coveredRequirements = false;
    }    
    
    public boolean canTryAnyway()
    {
        if (FactoryHolder._configManager.getStringValue("ALLOW_UNCOVERED_REQUIREMENTS_TRIAL").equals("false"))
            if (!this._coveredRequirements)
                return false;
            
        if (this._groupMembers.size() >= FactoryHolder._configManager.getNumberValue("MINIMUM_MEMBERS_TO_ATTEMPT_SOLVE"))
            return true;
        
        return false;
    }
    
    public boolean attemptSolve()
    {
        if (this.canTryAnyway())
        {
            double[] _saturationPoints = SigmoidedThrows.getSigmoidMap(this._formedFor.getDifficultyMap(), this.getTotalSkillMap());
            
            if (SigmoidedThrows.throwOnSigmoid(_saturationPoints)) {
                this._solvedChallenge = this._formedFor;
                return true;
            }
        }
        
        return false;
    }
    
    public void shareReputation()
    {
        int _shared = this._formedFor._reputationScore / this._groupMembers.size();
        
        for (SolverAgent i: this._groupMembers)
            if (this._solvedChallenge.isSolved())
                i._reputationScore += _shared;
            else
                i._reputationScore -= _shared;
    }
    
    public boolean hasSolvedLastChallenge()
    {
        return this._solvedChallenge != null;
    }
    
    public Challenge getSolvedChallenge()
    {
        return this._solvedChallenge;
    }
    
    public ArrayList<SolverAgent> getMembers()
    {
        return this._groupMembers;
    }
    
    public int[] getTotalSkillMap()
    {
        int[] _total = new int[FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size()];
        
        if (FactoryHolder._configManager.getStringValue("ENABLE_INDIVIDUAL_SKILL_COVERAGE").equals("true"))
        {
            for (int i = 0; i < this._groupMembers.size(); i++)
                 for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); k++)
                     if (_total[k] < this._groupMembers.get(i).getSkills().get(k).getExperience())
                         _total[k] = this._groupMembers.get(i).getSkills().get(k).getExperience();
        } else {
            for (int i = 0; i < this._groupMembers.size(); i++) 
                for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); k++)
                    _total[k] += this._groupMembers.get(i).getSkills().get(k).getExperience();

            for (int i = 0; i < _total.length; i++)
                _total[i] /= this._groupMembers.size();
        }
        
        return _total;
    }
    
    public int getTotalExperience()
    {
        int[] _skillMap = this.getTotalSkillMap();
        int _total = 0;
        
        for (int i = 0; i < _skillMap.length; i++)
            _total += _skillMap[i];
        
        return _total;
    }
}
