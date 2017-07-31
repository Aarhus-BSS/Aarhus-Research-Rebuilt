

library(readxl)
library(tidyverse)
library(stringr)
library(scales)
library(cowplot)


alpha_level = .2
data_folder = "D:\\simulation_results\\clean_output\\"
plot_which_type = "smartsorted"
plot_agent_chances = 3
plot_agent_prob = 33

read_agent_report <- function(filename) {
    read_excel(filename) %>%
        mutate(Agent = as.numeric(as.factor(Agent)),
            Round = as.integer(Round - 1),
            filename = filename)
}
read_problem_report <- function(filename) {
    read_excel(filename) %>%
        mutate(Problem = as.numeric(as.factor(Problem)),
            Round = as.integer(Round),
            filename = filename,
            `Solved` = 1 - `Solved`)
}
read_one_run <- function(agent_file, problem_file, path = test_path) {
    solvers = read_agent_report(str_c(path, agent_file))
    proposers = read_problem_report(str_c(path, problem_file))
    
    return(list(solvers, proposers))
}

read_all_runs <- function(preset) {
    folder = str_c(data_folder, preset, "\\Data\\")
    files <- list.files(folder, full.names = TRUE)
    problem_reports <- files[str_detect(files, "ProblemsReport")]
    agent_reports <- files[str_detect(files, "AgentReport")]
    
    problem_reports <- problem_reports %>%
        plyr::ldply(read_problem_report) %>%
        mutate(preset = preset,
            filename = as.numeric(as.factor(filename)))
    
    
    agent_reports <- agent_reports %>%
        plyr::ldply(read_agent_report) %>%
        mutate(preset = preset,
            filename = as.numeric(as.factor(filename)))
    
    list(agent_reports, problem_reports)
    
    
}



plot_all_agents <- function(preset, save=FALSE) {
    fil = read_all_runs(preset)
    
    solvers = fil[[1]] %>%
        group_by(Round, filename) %>%
        summarise(n = length(unique(Agent))) %>%
        mutate(who = "Solvers")
    proposers = fil[[2]] %>%
        group_by(Round, filename) %>%
        summarise(n = length(unique(Problem))) %>%
        mutate(who = "Proposers")
    
    number_of_agents = bind_rows(solvers, proposers) %>%
        mutate(who = factor(who, levels = c("Solvers", "Proposers")))

    
    plo = ggplot(number_of_agents, aes(Round, n, colour = who, group = filename, fill = who)) +
        geom_line(alpha = .4) +
        geom_line(aes(group = NULL), stat = "summary", fun.y = mean, size = 1) +
        # geom_point(alpha=.2, size=.5) +
        labs(y = "Number of agents",
            title = "Population",
            subtitle = preset) +
        guides(colour = guide_legend(
            title = element_blank(),
            override.aes = list(alpha = 1))) +
        theme(legend.position = "bottom") +
        scale_x_continuous(breaks = pretty_breaks()) +
        scale_y_continuous(breaks = pretty_breaks(),limits = c(0,NA)) +
        background_grid(major = "xy", minor = "none")
    
    if (save) {
        ggsave(str_c(data_folder, preset, "\\", preset, "_agents.png"), plo)
    } else {
        return(plo)
    }
}

plot_all_skills <- function(preset, save = FALSE) {
    fil = read_all_runs(preset)
    solvers = fil[[1]]
    proposers = fil[[2]]
    
    skill = solvers %>%
        group_by(Round, filename) %>%
        mutate(skill = as.numeric(`Composite Exp`),
            who = "Solvers") %>%
        bind_rows(group_by(proposers, Round, filename) %>%
                mutate(skill = as.numeric(Difficulty) / 4,
                    who = "Proposers")) %>%
        mutate(who = factor(who, levels = c("Solvers", "Proposers")))
    
    plo <- ggplot(skill, aes(Round, skill, colour = who, fill = who, group = filename)) +
        geom_line(stat="summary", fun.y=mean, alpha = .3) +
        geom_line(aes(group = NULL), stat="summary", fun.y=mean, size = 1) +
        # geom_ribbon(stat="summary", fun.data = mean_se, alpha = .3, colour = NA) +
        labs(y = "Skill / Difficulty",
            title = "Population skill / difficulty",
            subtitle = preset) +
        guides(colour = guide_legend(title = element_blank()),
            fill = "none") +
        theme(legend.position = "bottom") +
        scale_x_continuous(breaks = pretty_breaks()) +
        scale_y_continuous(breaks = pretty_breaks(),limits = c(0,NA)) +
        background_grid(major = "xy", minor = "none")
    
    if (save) {
        ggsave(str_c(data_folder, preset, "\\", preset, "_skills.png"), plo)
    } else {
        return(plo)
    }
    
    
}

plot_all_success <- function(preset, save=FALSE) {
    fil = read_all_runs(preset)
    solvers = fil[[1]]
    proposers = fil[[2]]
    
    success <- solvers %>%
        group_by(Round, filename) %>%
        summarise(success = mean(`Has Solved`)) %>%
        mutate(who = "Solvers") %>%
        bind_rows(group_by(proposers, Round, filename) %>%
                summarise(success = mean(Solved)) %>%
                mutate(who = "Proposers")) %>%
        mutate(who = factor(who, levels = c("Solvers", "Proposers")))
    
    # print(str(success))
    
    plo <- ggplot(success, aes(Round, success, colour = who, group = filename)) +
        geom_line(alpha = .3) +
        geom_line(aes(group = NULL), size = 1, stat = "summary", fun.y = mean) +
        labs(y = "Fraction of success",
            title = "Successful agents",
            subtitle = preset) +
        guides(colour = guide_legend(title = element_blank()),
            fill = "none") +
        theme(legend.position = "bottom") +
        scale_x_continuous(breaks = pretty_breaks()) +
        scale_y_continuous(labels = percent,limits = c(0,1)) +
        background_grid(major = "xy", minor = "none")
    
    if (save) {
        ggsave(str_c(data_folder, preset, "\\", preset, "_success.png"), plo)
    } else {
        return(plo)
    }
}

# x <- read_all_runs("random_sigmoid_2_2_60_60_33_3")

# plot_all_agents("random_sigmoid_2_2_60_60_33_3", save = FALSE)
# plot_all_skills("baseline", save = TRUE)
# plot_all_success("baseline", save = TRUE)

