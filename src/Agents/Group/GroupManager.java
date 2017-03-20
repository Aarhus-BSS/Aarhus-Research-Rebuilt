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
    
    private boolean _saturatedPool(ArrayList<SolverAgent> _solvers)
    {
        int _inGroups = 0;
        for (int i = 0; i < _solvers.size(); i++)
            if (_solvers.get(i)._isInGroup == true)
                _inGroups++;
        
        this._groupedAgents = _inGroups;
        
        if (_inGroups == _solvers.size())
            return true;
        
        return false;
    }
    
    public GroupManager(ArrayList<SolverAgent> _solvers, ArrayList<Challenge> _problems, int _round)
    {
        this._roundReference = _round;
        int _challengeIndex = 0;
        
        while (!this._saturatedPool(_solvers) 
                && _challengeIndex <= _problems.size())
        {
            _groupNode _tmp = new _groupNode();
            _tmp._group = new Group(_solvers, _problems.get(_challengeIndex));
            _tmp._problem = _problems.get(_challengeIndex);
            _tmp._challengeIndex = _challengeIndex;
            
            this._groups.add(_tmp);
        }
    }
    
    public int getGroupsCount()
    {
        return this._groups.size();
    }
}
