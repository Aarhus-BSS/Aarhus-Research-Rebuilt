/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

import static Agents.Group._MODEL_SETUP.MODEL_1A;
import static Agents.Group._MODEL_SETUP.MODEL_1B_WR;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import Common.Math.SigmoidedThrows;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private boolean _isAllowedIncompetency = false;
    private _MODEL_SETUP _model = MODEL_1A;
    
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
        for (int i = 0; i < this._formedFor.getDifficultyMap().length; i++)
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
        this._model = _model;
        
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
                                && _solvers.get(i)._solvedLastChallenge != true
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
                this._isAllowedIncompetency = true;
                break;
                
            case MODEL_1A_WR:
                
                break;
            case MODEL_1B_WR:
                this._isAllowedIncompetency = true;
                
                Collections.sort(_solvers, new Comparator<Object>() {
                    @Override
                    public int compare(Object a1, Object a2) {
                        SolverAgent a = (SolverAgent) a1, b = (SolverAgent) a2;
                        return b._reputationScore - a._reputationScore;
                    }
                });
                
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
        if (!this._isAllowedIncompetency)
            if (FactoryHolder._configManager.getStringValue("ALLOW_UNCOVERED_REQUIREMENTS_TRIAL").equals("false"))
                if (!this._coveredRequirements)
                    return false;
            
        if (this._groupMembers.size() >= FactoryHolder._configManager.getNumberValue("MINIMUM_MEMBERS_TO_ATTEMPT_SOLVE"))
            return true;
        
        return false;
    }
    
    private double[] _getDifference()
    {
        double[] _difference = new double[this._formedFor.getDifficultyMap().length];
        
        for (int i = 0; i < this._groupMembers.size(); i++)
            for (int k = 0; k < this._formedFor.getDifficultyMap().length; k++)
                _difference[k] += this._groupMembers.get(i).getSkills().get(k).getExperience();
        
        for (int i = 0; i < this._formedFor.getDifficultyMap().length; i++)
            _difference[i] -= this._formedFor.getDifficultyMap()[i];
        
        return _difference;
    }
    
    public double avgReputation()
    {
        double _avg = 0;
        for (int i = 0; i < this._groupMembers.size(); i++)
            _avg += this._groupMembers.get(i)._reputationScore;
        
        return _avg /= this._groupMembers.size();
    }
    
    public boolean attemptSolve()
    {
        if (this.canTryAnyway())
        {
            if (this._model.equals(MODEL_1A))
            {
                double[] _saturationPoints = SigmoidedThrows.getSigmoidMap(this._formedFor.getDifficultyMap(), this.getTotalSkillMap());

                if (SigmoidedThrows.throwOnSigmoid(_saturationPoints)) {
                    this._solvedChallenge = this._formedFor;
                    return true;
                }
                
            } else if (this._model.equals(MODEL_1B_WR)) {
                double[] _differentialSkill = this._getDifference();
                double _average = 0, _reqAvg = 0;
                
                for (int i = 0; i < _differentialSkill.length; i++)
                    _average += _differentialSkill[i];
                
                _average /= _differentialSkill.length;
                
                for (int i = 0; i < _differentialSkill.length; i++)
                    _reqAvg += this._formedFor.getDifficultyMap()[i];
                
                _reqAvg /= _differentialSkill.length;
                
                double _boosted = this.addBoost(this.avgReputation(), _average) + _average;
                
                double _sig = SigmoidedThrows.getSigmoidValue(_reqAvg, _boosted);
                
                if (SigmoidedThrows.throwOnSigmoid(_reqAvg)) {
                    this.shareReputation();
                    return true;
                }
            }
        }
        
        this._formedFor._idledRounds++;
        return false;
    }
    
    private double addBoost(double _reputation, double _skill)
    {
        if (_reputation <= 50)
            return (-1 * ((_reputation / 100) * _skill));
        else
            return ((_reputation / 100) * _skill);
    }
    
    private int _reputationExtraction()
    {
        int _reputationTotal = 0;
        
        this._formedFor.getTotalDifficulty();
        
        return 0;
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
