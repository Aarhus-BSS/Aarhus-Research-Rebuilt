/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

import static Agents.Group._MODEL_SETUP.MODEL_1A;
import Agents.SolverAgent;
import Challenge.Challenge;
import auresearch.FactoryHolder;
import java.util.ArrayList;

/**
 *
 * @author d3vil401
 */
public class GroupManager
{
    private class _groupNode
    {
        public Group _group = null;
        public Challenge _problem = null;
        public int _challengeIndex = -1;
    }
    
    private ArrayList<_groupNode> _groups = new ArrayList();
    private ArrayList<Group> _deadGroups = new ArrayList();
    private ArrayList<Challenge> _challenges = new ArrayList();
    private _MODEL_SETUP _currentModel = MODEL_1A;
    private int _groupedAgents = 0;
    private int _roundReference = -1;
    
    private boolean _saturedPool(ArrayList<SolverAgent> _solvers)
    {
        if (FactoryHolder._configManager.getStringValue("LIMIT_GROUPS_COUNT").equals("true"))
            if (this._groups.size() >= FactoryHolder._configManager.getNumberValue("MAXIMUM_EXISTING_GROUPS"))
                return true;
        
        return false;
    }
    
    private int _saturationScore = 0;
    
    private int _getSaturationLimit(ArrayList<SolverAgent> _solvers)
    {
        return _solvers.size() / 2;
    }
    
    public GroupManager(ArrayList<SolverAgent> _solvers, ArrayList<Challenge> _problems, int _round)
    {
        this._roundReference = _round;
        int _roundCounter = 0;
        int _solved = 0;
        
        for (int _challengeIndex = 0; _challengeIndex < _problems.size(); _challengeIndex++) 
        {
            _solvers.forEach((i) -> {
                i.resetForNewRound();
                this._saturationScore = 0;
            });
            
            if (!_problems.get(_challengeIndex).isSolved())
            {
                while (!this._saturedPool(_solvers))
                {
                    _groupNode _tmp = new _groupNode();
                    _tmp._group = new Group(_solvers, _problems.get(_challengeIndex), _MODEL_SETUP.fromString(FactoryHolder._configManager.getStringValue("GROUP_MODEL")));
                    _tmp._problem = _problems.get(_challengeIndex);
                    _tmp._challengeIndex = _challengeIndex;

                    if (_tmp._group._satured())
                        this._saturationScore++;
                    else {
                        this._groups.add(_tmp);
                        _roundCounter++;
                    }

                    if (this._saturationScore >= this._getSaturationLimit(_solvers))
                        break;
                }

                for (int i = 0; i < this._groups.size(); i++)
                    if (!this._groups.get(i)._problem.isSolved())
                        if (this._groups.get(i)._group.attemptSolve()) {
                            _solved++;
                            for (SolverAgent x: this._groups.get(i)._group.getMembers())
                                _problems.get(_challengeIndex).forceAssignSuccess(x);
                            _problems.get(_challengeIndex)._isGroupSolved = true;
                        }

                for (int i = 0; i < this._groups.size(); i++)
                    this._groups.get(i)._group.disband();
            }
        }
        
    }
    
    public int getGroupsCount()
    {
        return this._groups.size();
    }
}
