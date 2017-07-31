

library(readxl)
library(tidyverse)
library(stringr)
library(scales)
library(cowplot)


alpha_level = .2
data_folder = "D:\\simulation_results\\clean_output\\"
plot_which_type = "sorted"
plot_agent_chances = 20
plot_agent_prob = 50
plot_mutations = "Low mutation rate (2%)"
# plot_mutations = "High mutation rate (50%)"

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

parse_params_from_preset <- function(preset) {
    result_ <- str_split(preset, "_", simplify = T) %>% data.frame()
    names(result_) <- c("type", "sigmoid", "sa_mutate", "ch_mutate", "sa_max", "ch_max", "agent_generation_probability", "agent_generation_chances")

    result_ %>%
        mutate(
            preset = preset,
            heterogenity_sa = ifelse(sa_max == 60, "Homogenous solvers (40-60)", "Heterogenous solvers (0-100)"),
            heterogenity_ch = ifelse(ch_max == 60, "Homogenous challenges (40-60)", ifelse(ch_max == 80, "Homogenous challenges (60-80)", "Heterogenous challenges (0-100)")),
            mutations = ifelse(sa_mutate == 2, "Low mutation rate (2%)", "High mutation rate (50%)"),
            # agent_generation_chances = factor(agent_generation_chances, levels = c(3,5,10), 
            #     labels = c("3 new agents per round", "5 new agents per round", "10 new agents per round")),
            sigmoid = factor(sigmoid, levels = c("dicethrow", "sigmoid"),
                labels = c("Sharp cutoff", "Sigmoid")))
    }


presets <- list.files(data_folder, include.dirs = TRUE) %>%
    data.frame(preset = .) %>%
    filter(str_detect(preset, "20"))
    # parse_params_from_preset() %>%
    # sample_n(2)

# 
# solvers <- plyr::adply(presets, 1, function(ro) {
#     # print(ro$preset)
#     folder = str_c(data_folder, ro[["preset"]], "\\Data\\")
#     files <- list.files(folder, full.names = TRUE)
#     agent_reports <- files[str_detect(files, "AgentReport")]
#     if (length(agent_reports) == 0) {
#         return(NULL)
#     }
# 
#     agent_reports %>%
#         plyr::ldply(read_agent_report) %>%
#         # as_data_frame() %>%
#         # cbind(parse_params_from_preset(preset))
#         mutate(preset = ro$preset,
#             filename = as.numeric(as.factor(filename)))
# }, .progress = "text") %>% dplyr::mutate(who = factor("Solvers", levels = c("Solvers", "Proposers")))
# #
# write_csv(solvers, path = str_c(data_folder, "../solvers2.csv"))
# 
# # rm(solvers)
# 
# 
# proposers <- plyr::adply(presets, 1, function(ro) {
#     folder = str_c(data_folder, ro[["preset"]], "\\Data\\")
#     files <- list.files(folder, full.names = TRUE)
#     problem_reports <- files[str_detect(files, "ProblemsReport")]
#         if (length(problem_reports) == 0) {
#             return(NULL)
#         }
# 
#     problem_reports %>%
#         plyr::ldply(read_problem_report) %>%
#         mutate(preset = ro$preset,
#             filename = as.numeric(as.factor(filename)))
#         # as_data_frame() %>%
#         # cbind(parse_params_from_preset(preset))
# }, .progress = "text") %>% mutate(who = factor("Proposers", levels = c("Solvers", "Proposers")))
# 
# 
# write_csv(proposers, path = str_c(data_folder, "../proposers2.csv"))
# rm(proposers)



solvers = read_csv(str_c(data_folder, "../solvers.csv"))




solvers = solvers %>%
    # number_of_agents %>%
    filter(type == plot_which_type,
        mutations %in% c(plot_mutations),
        plot_agent_prob == 50)


proposers = read_csv(str_c(data_folder, "../proposers.csv"))%>%
    parse_params_from_preset()
proposers = proposers %>%
    # number_of_agents %>%
    filter(Type == plot_which_type,
        mutations %in% c(plot_mutations),
        agent_generation_probability == 50)

params = presets %>%
    data.frame(preset = .) %>%
    mutate(params = purrr::map(preset, parse_params_from_preset)) %>%
    unnest()

solvers = left_join(solvers, params) %>%
    mutate(who = factor("Solver", levels = c("Solver", "Challenge")),
        filename = factor(filename, levels = 1:20))
proposers = left_join(proposers, params) %>%
    mutate(who = factor("Challenge", levels = c("Solver", "Challenge")),
        filename = factor(filename, levels = 1:20))



# calculate 

simulations_alive <- solvers %>%
    distinct(preset, filename, Round) %>%
    group_by(preset, Round) %>%
    count() %>%
    rename(n_sims_alive = n)  %>%
    # summarise(average_rounds = n()) %>%
    ungroup() %>%
    complete(Round, preset ,fill = list(n_sims_alive = 0))



## plot no of agents 
    
number_of_agents = bind_rows(solvers, proposers) %>%
    dplyr::count(preset, Round, filename, type, heterogenity_sa, heterogenity_ch, mutations, who, sigmoid, agent_generation_chances, agent_generation_probability) %>%
    left_join(simulations_alive)
    # dplyr::summarise(n = n()) %>%
    # sample_n(10000)

plot_pop <- number_of_agents %>%
# number_of_agents %>%
    filter(type == plot_which_type,
        mutations %in% c(plot_mutations),
        plot_agent_prob == 50) %>%
    ggplot(aes(Round, n, colour = who, group = str_c(filename, who), fill = who)) +
    # geom_ribbon(aes(Round, ymin = 0, ymax = n, group = NULL, colour = NULL, fill = NULL), data = simulations_alive, alpha = .3) +
    geom_line(aes(alpha = n_sims_alive)) +
    geom_line(aes(group = NULL, size = n_sims_alive), stat = "summary", fun.y = mean) +
    # geom_point(alpha=.2, size=.5) +
    labs(y = "Number of agents",
        title = "Population",
        subtitle = str_c("Algorithm: ", plot_which_type, ", ", plot_mutations)) +
    guides(colour = guide_legend(
        title = element_blank(),
        override.aes = list(alpha = 1))) +
    theme(legend.position = "bottom") +
    scale_x_continuous(breaks = pretty_breaks()) +
    scale_y_continuous(breaks = pretty_breaks(),limits = c(0,NA)) +
    background_grid(major = "xy", minor = "none") +
    facet_grid(sigmoid ~ heterogenity_ch + heterogenity_sa, scales = "free") +
    scale_size(range = c(0,2)) +
    scale_alpha(range = c(0,.4))

# plot_pop
# ggsave("x.png", height = 15, width = 20)

save_plot(str_c(plot_which_type, "_all_agents.png"), plot_pop, base_height = 13, base_width = 20)


# 
# ## plot skills
# 
skill = solvers %>%
    group_by(preset, Round, filename, type, heterogenity_sa, heterogenity_ch, mutations, who, sigmoid, agent_generation_chances, agent_generation_probability) %>%
    mutate(skill = as.numeric(`Composite Exp`)) %>%
    bind_rows(group_by(proposers, preset, Round, filename, type, heterogenity_sa, heterogenity_ch, mutations, who, sigmoid, agent_generation_chances, agent_generation_probability) %>%
            mutate(skill = as.numeric(Difficulty) / 4)) %>%
    left_join(simulations_alive)

plot_skill <- skill %>%
    filter(type == plot_which_type,
        mutations %in% c(plot_mutations),
        plot_agent_prob == 50) %>%
    ggplot(aes(Round, skill, colour = who, fill = who, group = str_c(filename, who))) +
    geom_line(aes(alpha = n_sims_alive), stat = "summary", fun.y=mean) +
    geom_line(aes(group = NULL, size = n_sims_alive), stat="summary", fun.y=mean) +
    # geom_ribbon(stat="summary", fun.data = mean_se, alpha = .3, colour = NA) +
    labs(y = "Skill / Difficulty",
        title = "Population skill / difficulty",
        subtitle = str_c("Algorithm: ", plot_which_type, ", ", plot_mutations)) +
    guides(colour = guide_legend(title = element_blank()),
        fill = "none") +
    theme(legend.position = "bottom") +
    scale_x_continuous(breaks = pretty_breaks()) +
    scale_y_continuous(breaks = pretty_breaks(),limits = c(0,NA)) +
    background_grid(major = "xy", minor = "none") +
    facet_grid(sigmoid ~ heterogenity_ch + heterogenity_sa, scales = "free") +
    scale_size(range = c(0,2)) +
    scale_alpha(range = c(0,.4))


save_plot(str_c(plot_which_type, "_all_skills.png"), plot_skill, base_height = 13, base_width = 20)
# 
# 
# ## plot fraction of successes
# 
success <- solvers %>%
    group_by(filename, preset, Round, type, heterogenity_sa, heterogenity_ch, mutations, who, sigmoid, agent_generation_chances, agent_generation_probability) %>%
    summarise(success = mean(`Has Solved`)) %>%
    bind_rows(group_by(proposers, preset, Round, filename, type, heterogenity_sa, heterogenity_ch, mutations, who, sigmoid, agent_generation_chances, agent_generation_probability) %>%
            summarise(success = mean(Solved))) %>%
    left_join(simulations_alive)


plot_success <- success %>%
    filter(type == plot_which_type,
        mutations %in% c(plot_mutations),
        plot_agent_prob == 50) %>%
    ggplot(aes(Round, success, colour = who, group = str_c(filename, who))) +
    geom_line(aes(alpha = n_sims_alive)) +
    geom_line(aes(group = NULL, size = n_sims_alive), stat = "summary", fun.y = mean) +
    labs(y = "Fraction of success",
        title = "Successful agents",
        subtitle = str_c("Algorithm: ", plot_which_type, ", ", plot_mutations)) +
    guides(colour = guide_legend(title = element_blank()),
        fill = "none") +
    theme(legend.position = "bottom") +
    scale_x_continuous(breaks = pretty_breaks()) +
    scale_y_continuous(labels = percent,limits = c(0,1)) +
    background_grid(major = "xy", minor = "none") +
    facet_grid(sigmoid ~ heterogenity_ch + heterogenity_sa, scales = "free") +
    scale_size(range = c(0,2)) +
    scale_alpha(range = c(0,.4))

save_plot(str_c(plot_which_type, "_all_success.png"), plot_success, base_height = 13, base_width = 20)

