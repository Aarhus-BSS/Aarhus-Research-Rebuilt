# -*- coding: utf-8 -*-

"""
Generate the options files
"""


import os


base = """
; ======== Configuration Scenario Settings ========

; Will appear on GUI list.
SCENARIO_NAME="{name}"

; For compatibility checks.
SCENARIO_VERSION=4

; =================================================


; ======== Internal parameters ========

; library: GUI classic view.
; console: Multi-Master Running for big data generation and analysis, this is using multithreading!
;		   NMasterRuns / 5 (number of RoundManager for each thread) = Threads number
;MODE=library
MODE=console

MASTER_ITERATIONS=10

;MERGING_ALPHA=0.3

REPORT_OUTPUT_FOLDER=..\\Output\\{name}\\Data
GRAPH_OUTPUT_FOLDER=..\\Output\\{name}\\Graphs
PRESET_INPUT_FOLDER=..\\Presets\\

; =====================================

; ========= Environmental parameters ==========

NUMBER_OF_CHANCES_AN_AGENT_HAS_TO_TRY_TO_FIND_A_PROBLEM=3

NUMBER_OF_TRIALS_FOR_SINGLE_AGENT_SOLVING=55

NUMBER_OF_CHANCES_A_GROUP_HAS_TO_TRY_TO_FIND_A_PROBLEM=3

MORTALITY_RATE=0

; =============================================

; ========= Agents parameters =========

;AGENT_SKILLS=chemistry,biology,mathematics,physics,medicine,economics,language,management,business,it
AGENT_SKILLS=chemistry,biology,mathematics,physics

; =====================================

; ========= Challenge parameters =========

REQUIREMENT_AVERAGE_BASED=false

CH_ENABLE_MUTATION=true

CH_MUTATION_SIGN=+/-

CH_MUTATION_RATE={ch_mutate}

CH_DEADLINE=1.0

CH_EASYREJECTOR=false

CH_MINIMAL_DIFFERENCE=1

ENABLE_MAX_IDLED_ROUNDS_CHALLENGE=true

CH_MAX_IDLE_ROUNDS=5

;CH_MIN_REWARD=1

;CH_MAX_REWARD=200

; How to rank the problems in the sorted method
;	- HTL : Highest To Lowest (difficulty)
;	- LTH : Lowest To Highest (difficulty)
CH_SORTED_RANK_TYPE=HTL

; ========================================

; ======== Solver Agent configurations ========

; Minimum experience.
SA_MINIMUM_EXPERIENCE=1

; Maximum randomized experience for each skill.
SA_MAXIMUM_EXPERIENCE=100

SA_ENABLE_MUTATION_RATE=true

SA_MUTATION_RATE_VALUE={sa_mutate}

; Mutation rate sign:
;	- +/-
;	- +
;	- -
SA_MUTATION_RATE_SIGN=+/-

SA_MAX_IDLED_ROUNDS=5

; How to rank the agents in the sorted method
;	- HTL : Highest To Lowest (skill)
;	- LTH : Lowest To Highest (skill)
SA_SORTED_RANK_TYPE=HTL

; =============================================

; ======== Clonation parameters ========

SA_EXPONENTIAL_GENERATION_CHANCE=3

PA_EXPONENTIAL_GENERATION_CHANCE=3

SA_CHANCE_OF_GENERATION=33

PA_CHANCE_OF_GENERATION=33

SA_ENABLE_MAX_CLONATION=false

SA_NOT_A_CLONE=false

SA_MAX_CLONATIONS=3

; ======================================

; ======== Proposer Agent parameters ========

; Maximum of randomized requirements experience for the challenge.
MAXIMUM_RANDOM_REQUIREMENTS_SELECTED=4

MINIMUM_RANDOM_EXPERIENCE_PER_REQUIREMENT=1

; Max amount of exp for each skill inside a challenge.
MAXIMUM_RANDOM_EXPERIENCE_PER_REQUIREMENT=100

PA_NOT_A_CLONE=false

; Mutation rate sign:
;	- +/-
;	- +
;	- -
PA_MUTATION_RATE_SIGN=+/-

; ===========================================

; ======== Group parameters ========

ENABLE_GROUPS=false

USE_SIGMOID=true

MAX_GROUP_MEMBERS=4

SINGULAR_SKILL_SELECTION=true

ALLOW_UNCOVERED_REQUIREMENTS_TRIAL=true

MINIMUM_MEMBERS_TO_ATTEMPT_SOLVE=2

LIMIT_GROUPS_COUNT=true

MAXIMUM_EXISTING_GROUPS=30

ENABLE_INDIVIDUAL_SKILL_COVERAGE=true

; Available models:
;	- 1A
;	- 1B - NA
;	- 1A_WR <- "With Reputation" - NA
;	- 1B_WR - NA
GROUP_MODEL=1A

; ==================================

; ======= REPUTATION ==============

ENABLE_REPUTATION=false

; =================================

; ======== Composite parameters ===

MINIMAL_RANGE={min}

MAXIMAL_RANGE={max}

; =================================

; ======== Game parameters ========

; How many runs the same agents effectively do in the same round.
; 1 is considered default.
SUBROUND_ITERATIONS=1

; How many rounds do we have.
MAX_ROUNDS=50

; Deadline limit in minutes.
DEADLINE=1.0

; How many proposer agents do we have?
PROPOSER_AGENTS_AMOUNT=85

; How many solver agents do we have?
SOLVER_AGENTS_AMOUNT=100

; "Game" Type selected, existing are:
;	- random: SAgent picks a random problem and try to solve it.
;	- sorted: SAgent picks a problem by a sorted list.
;	- smart_sorted: SAgent will try to solve all the problems until success.
GAME_TYPE={type}

; Individual SolverAgent will use this method to try this challenge.
; 	- dicethrow (old one)
;	- sigmoid
ATTEMPT_TYPE={sigmoid}

; =================================
"""

base_settings = {'sigmoid': 'dicethrow',
                 'type': 'smart_sorted',
                 'min': 1,
                 'max': 100,
                 'sa_mutate': 5,
                 'ch_mutate': 2}


preset_folder = "\\\\uni.au.dk\\Users\\au540041\\Documents\\Aarhus-Research-Rebuilt\\Presets\\"
output_folder = "\\\\uni.au.dk\\Users\\au540041\\Documents\\Aarhus-Research-Rebuilt\\Output\\"


for typ in [{'type': 'smart_sorted'}, {'type': 'sorted'}, {'type': 'random'}]:
    for het in [{'min': 1, 'max': 100}, {'min': 40, 'max': 60}]:
        for mut in [{'sa_mutate': 5, 'ch_mutate': 2}, {'sa_mutate': 50, 'ch_mutate':50}]:
                params = {**base_settings, **typ, **het, **mut}
                name = '_'.join([params['type'], str(params['max']), str(params['sa_mutate']),str(params['ch_mutate'])])
                params['name'] = name
                
                with open(preset_folder + name + ".cfg", "w") as f:
                    f.write(base.format(**params))
                    
                if not os.path.exists(output_folder + name):
                    os.makedirs(output_folder + name)
                    os.makedirs(output_folder + name + "\\Data")
                    os.makedirs(output_folder + name + "\\Graphs")
                    












