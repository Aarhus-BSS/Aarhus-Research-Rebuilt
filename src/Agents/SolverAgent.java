/*
 * Decompiled with CFR 0_118.
 */
package Agents;

import Agents.Properties.cSkill;
import Agents.Properties.cStatistics;
import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Random;

public class SolverAgent
implements Comparable<SolverAgent> {
    private ArrayList<cSkill> _skills = new ArrayList();
    private cStatistics _stats = new cStatistics();
    private Random _random = new Random();
    private boolean _solvedLastChallenge = false;
    private int _tryHardedLastChallenge = 0;
    public boolean _isInGroup = false;

    public void _setupAgent() {
        int i;
        for (i = 0; i < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++i) {
            this._skills.add(new cSkill(FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").get(i).toString()));
        }
        for (i = 0; i < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++i) {
            this._skills.get(i).setExperience(this._random.nextInt(FactoryHolder._configManager.getNumberValue("SA_MAXIMUM_EXPERIENCE") + 1));
        }
        this._stats._money = 0;
        this._stats._successTrials = 0;
        this._stats._trials = 0;
        this._stats._rejected = 0;
        this._isInGroup = false;
    }

    public void _setupAgent(ArrayList<cSkill> _skills) {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Creating new Agent (prebuilt skills)");
        this._stats._money = 0;
        this._stats._successTrials = 0;
        this._stats._trials = 0;
        this._stats._rejected = 0;
        this._isInGroup = false;
        this._skills = (ArrayList)_skills.clone();
    }

    public cStatistics getStats() {
        return this._stats;
    }

    public void resetForNewRound() {
        this._solvedLastChallenge = false;
        this._tryHardedLastChallenge = 0;
        this._isInGroup = false;
    }

    public void setSolvedLastChallenge(boolean _status) {
        this._solvedLastChallenge = _status;
    }

    public void setTryHarder(int _status) {
        this._tryHardedLastChallenge = _status;
    }

    public boolean getHasSolvedLastChallenge() {
        return this._solvedLastChallenge;
    }

    public int getTryHarded() {
        return this._tryHardedLastChallenge;
    }

    public SolverAgent() {
        this._setupAgent();
    }

    public SolverAgent(ArrayList<cSkill> _skills) {
        this._setupAgent(_skills);
    }

    @Override
    public int compareTo(SolverAgent _agent) {
        return _agent.getTotalExperience() - this.getTotalExperience();
    }

    public void addExpToSkill(String _skillName, int _experienceAmount) {
        for (int i = 0; i < this._skills.size(); ++i) {
            if (!this._skills.get(i).getName().equals(_skillName)) continue;
            this._skills.get(i).addExperience(_experienceAmount);
            return;
        }
    }

    public boolean isCompetent(String _skillName) {
        for (int i = 0; i < this._skills.size(); ++i) {
            if (!this._skills.get(i).getName().equals(_skillName) || this._skills.get(i).getExperience() < FactoryHolder._configManager.getNumberValue("SA_MINIMAL_COMPETENCY_EXPERIENCE")) continue;
            return true;
        }
        return false;
    }

    public void giveReward(int _amount) {
        this._stats._money += _amount;
    }

    public cSkill getSkill(String _skillName) {
        for (int i = 0; i < this._skills.size(); ++i) {
            if (!this._skills.get(i).getName().equals(_skillName)) continue;
            return this._skills.get(i);
        }
        return null;
    }

    public int getTotalExperience() {
        int _total = 0;
        for (int i = 0; i < this._skills.size(); ++i) {
            _total += this._skills.get(i).getExperience();
        }
        return _total;
    }

    private ArrayList<cSkill> _mutatePositiveSkills() 
    {
        ArrayList _mutatedSkills = (ArrayList)this._skills.clone();
        int _outExp = 0;
        int _curExp = 0;
        for (int i = 0; i < this._skills.size(); ++i) 
        {
            _curExp = this._skills.get(i).getExperience();
            _outExp = _curExp + FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") * _curExp / 100;
            ((cSkill)_mutatedSkills.get(i)).setExperience(_outExp);
        }
        return _mutatedSkills;
    }

    private cSkill _mutateSkill(cSkill _oldSkill, String _rateoSign)
    {
        cSkill _newSkill = _oldSkill.clone();
        int _outExp = 0;
        int _curExp = _oldSkill.getExperience();
        if (_rateoSign.equals("+/-")) {
            boolean _sign = this._random.nextBoolean();
            _outExp = _sign ? _curExp + FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") * _curExp / 100 : _curExp - FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") * _curExp / 100;
        } else if (_rateoSign.equals("+")) {
            _outExp = _curExp + FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") * _curExp / 100;
        } else if (_rateoSign.equals("-")) {
            _outExp = _curExp - FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE") * _curExp / 100;
        } else {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Rateo sign is not recognized.");
        }
        _newSkill.setExperience(_outExp);
        return _newSkill;
    }

    public void addExperience(int _amount) {
        this._stats._generalExperience += _amount;
    }

    private ArrayList<cSkill> _mutateNegativeSkills() {
        int _factor = this._random.nextInt(FactoryHolder._configManager.getNumberValue("SA_MUTATION_RATE_VALUE"));
        ArrayList _mutatedSkills = (ArrayList)this._skills.clone();
        for (int i = 0; i < this._skills.size(); ++i) {
            ((cSkill)_mutatedSkills.get(i)).setExperience((this._skills.get(i).getExperience() - _factor) * (this._skills.get(i).getExperience() / 100));
        }
        return _mutatedSkills;
    }

    public String compositeExperience() {
        double _comp = 0.0;
        for (int i = 0; i < this._skills.size(); ++i) {
            _comp += (double)this._skills.get(i).getExperience();
        }
        return String.valueOf(_comp / (double)this._skills.size());
    }

    public ArrayList<cSkill> getSkills() {
        return this._skills;
    }

    public SolverAgent clone() {
        ArrayList<cSkill> _newSet = new ArrayList<cSkill>();
        cSkill _newSkillSlot = null;
        ++this._stats._clonedTimes;
        if (FactoryHolder._configManager.getStringValue("SA_ENABLE_MUTATION_RATE").equals("true")) {
            for (int i = 0; i < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++i) {
                _newSkillSlot = this._mutateSkill(this._skills.get(i), FactoryHolder._configManager.getStringValue("SA_MUTATION_RATE_SIGN"));
                _newSet.add(_newSkillSlot);
            }
            return new SolverAgent(_newSet);
        }
        return new SolverAgent(this._skills);
    }

    public void _tick() {
        for (int i = 0; i < this._skills.size(); ++i) {
            this._skills.get(i)._tick();
        }
    }

    public void _incrementRejected() {
        ++this._stats._rejected;
        ++this._tryHardedLastChallenge;
    }
}

