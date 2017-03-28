/*
 * Decompiled with CFR 0_118.
 */
package Challenge;

import Agents.Properties.cSkill;
import Agents.ProposerAgent;
import Agents.SolverAgent;
import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Random;

public class Challenge
implements Comparable<Challenge> {
    private ArrayList<cSkill> _skillTypes = new ArrayList();
    private int[] _difficultyMap = null;
    private int _reward = 0;
    private int _totalDifficulty = 0;
    private ProposerAgent _author = null;
    private Random _random = new Random();
    private ArrayList<SolverAgent> _solvers = new ArrayList();
    private ArrayList<SolverAgent> _tryHarders = new ArrayList();
    private ArrayList<SolverAgent> _tryHarderRejected = new ArrayList();
    private boolean _isSolved = false;
    public int _idledRounds = 0;
    public boolean _isGroupSolved = false;

    private int _getBound() {
        if (this._difficultyMap.length <= FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size()) {
            return this._difficultyMap.length;
        }
        return FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size();
    }

    private void _calculateTotalDifficulty() {
        for (int i = 0; i < this._getBound(); ++i) {
            this._totalDifficulty += this._difficultyMap[i];
        }
    }

    public int getReward() {
        return this._reward;
    }

    public int getTotalDifficulty() {
        return this._totalDifficulty;
    }

    public int[] getDifficultyMap() {
        return this._difficultyMap;
    }

    public ProposerAgent getAuthor() {
        return this._author;
    }

    public ArrayList<SolverAgent> getSolver() {
        return this._solvers;
    }

    public boolean hasSolved(SolverAgent _agent) {
        for (int i = 0; i < this._solvers.size(); ++i) {
            if (!this._solvers.get(i).equals(_agent)) continue;
            return true;
        }
        return false;
    }

    public boolean isSolved() {
        return this._isSolved;
    }

    public void setSolvedStatus(boolean _status) {
        this._isSolved = _status;
    }

    public ArrayList<SolverAgent> getTryHarders() {
        return this._tryHarders;
    }

    public Challenge(ArrayList<cSkill> _requirements, int[] _difficulty, boolean _solved, ProposerAgent _author) {
        this._skillTypes = _requirements;
        if (_difficulty.length != FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size()) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Difficulty map is different from skill count registered!");
        }
        this._difficultyMap = _difficulty;
        this._calculateTotalDifficulty();
        this._reward = this._totalDifficulty / 2;
        this._author = _author;
        this._isGroupSolved = false;
    }

    private void _mutatePositive() {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        for (int i = 0; i < this._getBound(); ++i) {
            int[] arrn = this._difficultyMap;
            int n = i;
            arrn[n] = arrn[n] + _rate * this._difficultyMap[i] / 100;
        }
    }

    private void _mutateNegative() {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        for (int i = 0; i < this._getBound(); ++i) {
            int[] arrn = this._difficultyMap;
            int n = i;
            arrn[n] = arrn[n] - _rate * this._difficultyMap[i] / 100;
        }
    }

    private void _mutateNegative(int _index) {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        this._difficultyMap[_index] = (this._difficultyMap[_index] - _rate) * (this._difficultyMap[_index] / 100);
    }

    private void _mutatePositive(int _index) {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        this._difficultyMap[_index] = (this._difficultyMap[_index] + _rate) * (this._difficultyMap[_index] / 100);
    }

    public void mutate() {
        if (FactoryHolder._configManager.getStringValue("CH_ENABLE_MUTATION").equals("true")) {
            if (FactoryHolder._configManager.getStringValue("CH_MUTATION_SIGN").equals("+/-")) {
                boolean _throw = this._random.nextBoolean();
                for (int i = 1; i < this._getBound(); ++i) {
                    if (_throw) {
                        this._mutatePositive(i);
                        continue;
                    }
                    this._mutateNegative(i);
                }
            } else if (FactoryHolder._configManager.getStringValue("CH_MUTATION_SIGN").equals("-")) {
                for (int i = 1; i < this._getBound(); ++i) {
                    this._mutateNegative(i);
                }
            } else if (FactoryHolder._configManager.getStringValue("CH_MUTATION_SIGN").equals("+")) {
                for (int i = 1; i < this._getBound(); ++i) {
                    this._mutatePositive(i);
                }
            } else {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Mutation sign for challenge is unrecognized.");
            }
        }
        this._calculateTotalDifficulty();
        this._reward = this._totalDifficulty / 2;
        this._solvers.clear();
        this._tryHarders.clear();
        this._isSolved = false;
        this._idledRounds = 0;
        this._isGroupSolved = false;
    }

    @Override
    public int compareTo(Challenge _challenge) {
        return this._totalDifficulty - _challenge._totalDifficulty;
    }

    public boolean attemptSolve(SolverAgent _agent) {
        double _chance = 1.0;
        double _randomer = 0.0;
        int i = 0;
        if (i < this._getBound()) 
        {
            _randomer = this._random.nextDouble();
            if (_agent.getSkill(this._skillTypes.get(i).getName()).getExperience() != 0) 
            {
                if (_agent.getSkill(this._skillTypes.get(i).getName()).getExperience() - this._difficultyMap[i] >= FactoryHolder._configManager.getNumberValue("CH_MINIMAL_DIFFERENCE") && !FactoryHolder._configManager.getStringValue("CH_EASYREJECTOR").equals("true")) {
                    this._solvers.add(_agent);
                    _agent.giveReward(this._reward);
                    this._author.setLastSolved(true);
                    this._isSolved = true;
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void forceSolver(SolverAgent _agent) {
        this._solvers.add(_agent);
    }

    public void forceAssignSuccess(SolverAgent _agent) {
        this._isSolved = true;
        this.forceSolver(_agent);
        this._tryHarders.remove(_agent);
        _agent.giveReward(this._reward);
        this._author.setLastSolved(true);
    }

    public String getCompositeString() {
        double compositeCounter = 0.0;
        for (int i = 0; i < this._difficultyMap.length; ++i) {
            compositeCounter += (double)this._difficultyMap[i];
        }
        return String.valueOf(compositeCounter / (double)this._difficultyMap.length);
    }
}

