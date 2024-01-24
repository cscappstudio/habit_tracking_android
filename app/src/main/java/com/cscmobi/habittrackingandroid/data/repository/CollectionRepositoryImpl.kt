package com.cscmobi.habittrackingandroid.data.repository

import android.content.Context
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat

class CollectionRepositoryImpl(val context: Context) : CollectionRepository {
    private val morningTasks = mutableListOf<Task>(
        Task(
            name = "Practice affirmations",
            ava = context.resources.getResourceEntryName(R.drawable.emoji_sing_right_note),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.media_image_list),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Meditate",
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Make the bed",
            ava = context.resources.getResourceEntryName(R.drawable.bed),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Brush teeth",
            ava = context.resources.getResourceEntryName(R.drawable.fluent_mdl2_teeth),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Drink water",
            ava = context.resources.getResourceEntryName(R.drawable.glass_half),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Eat breakfast",
            ava = context.resources.getResourceEntryName(R.drawable.bread_slice),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Pray",
            ava = context.resources.getResourceEntryName(R.drawable.flower),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ), Task(
            name = "Take a shower",
            ava = context.resources.getResourceEntryName(R.drawable.bathroom),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
        Task(
            name = "Take vitamins",
            ava = context.resources.getResourceEntryName(R.drawable.apple),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine"
        ),
    )


    private val nightTimeTasks = mutableListOf<Task>(
        Task(
            name = "Warm and cozy drink",
                ava = context.resources.getResourceEntryName(R.drawable.bbq),
                repeate = TaskRepeat(true,"daily",1),
                tag = "Nighttime"

        ),
        Task(
            name = "Skincare",
            ava = context.resources.getResourceEntryName(R.drawable.soap),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime"
        ),
        Task(
            name = "Reflect on my day",
            ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime"
        ),
        Task(
            name = "Stretch",
            ava = context.resources.getResourceEntryName(R.drawable.stretching),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime"
        ),
        Task(
            name = "Aroma candle",
            ava = context.resources.getResourceEntryName(R.drawable.fire_flame),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime"
        ),
        Task(
            name = "Meditate",
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_2),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime"
        ),
    )

    private val dailyChoreTasks = mutableListOf<Task>(
        Task(
            name = "Make bed",
            ava = context.resources.getResourceEntryName(R.drawable.crib),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores"
        ),
        Task(
            name = "Laundry",
            ava = context.resources.getResourceEntryName(R.drawable.washing_machine),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores"
        ),
        Task(
            name = "Sweep or Vacuum",
            ava = context.resources.getResourceEntryName(R.drawable.pacman),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores"
        ),
        Task(
            name = "Task out trash",
            ava = context.resources.getResourceEntryName(R.drawable.bin_half),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores"
        ),
        Task(
            name = "Meal prep",
            ava = context.resources.getResourceEntryName(R.drawable.cutlery),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores"
        ),
        Task(
            name = "Water plants",
            ava = context.resources.getResourceEntryName(R.drawable.soil_alt),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores"
        ),
    )


    private val healthyTasks = mutableListOf<Task>(
        Task(
            name = "Fruit and veggies",
            ava = context.resources.getResourceEntryName(R.drawable.frame_48),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle"
        ),
        Task(
            name = "Drink water",
            ava = context.resources.getResourceEntryName(R.drawable.glass_half_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle"
        ),
        Task(
            name = "Limit sugar",
            ava = context.resources.getResourceEntryName(R.drawable.half_cookie),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle"
        ),
        Task(
            name = "No alcohol",
            ava = context.resources.getResourceEntryName(R.drawable.glass_empty),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle"
        ),
        Task(
            name = "Get enough sleep",
            ava = context.resources.getResourceEntryName(R.drawable.small_lamp),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle"
        ),
        Task(
            name = "Take vitamins",
            ava = context.resources.getResourceEntryName(R.drawable.apple_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle"
        ),
    )


    private val studyTasks = mutableListOf<Task>(
        Task(
            name = "Set goals",
            ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups"
        ),
        Task(
            name = "Review material",
            ava = context.resources.getResourceEntryName(R.drawable.bookmark_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups"
        ),
        Task(
            name = "Take note",
            ava = context.resources.getResourceEntryName(R.drawable.favourite_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups"
        ),
        Task(
            name = "Brainstorming",
            ava = context.resources.getResourceEntryName(R.drawable.brain),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups"
        ),
        Task(
            name = "Prioritize tasks",
            ava = context.resources.getResourceEntryName(R.drawable.rocket),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups"
        ),
        Task(
            name = "Study groups",
            ava = context.resources.getResourceEntryName(R.drawable.graduation_cap),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups"
        ),
    )


    private val stressreliefTasks = mutableListOf<Task>(
        Task(
            name = "Set goals",
            ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Drink tea",
            ava = context.resources.getResourceEntryName(R.drawable.coffee_cup),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Meditate",
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_1),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Watch movie",
            ava = context.resources.getResourceEntryName(R.drawable.modern_tv),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Music on",
            ava = context.resources.getResourceEntryName(R.drawable.headset),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Stretch",
            ava = context.resources.getResourceEntryName(R.drawable.stretching_1),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Yoga",
            ava = context.resources.getResourceEntryName(R.drawable.yoga),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Paint or draw",
            ava = context.resources.getResourceEntryName(R.drawable.palette),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
        Task(
            name = "Read",
            ava = context.resources.getResourceEntryName(R.drawable.open_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief"
        ),
    )


    private val stayingfitTasks = mutableListOf<Task>(
        Task(
            name = "Jogging",
            ava = context.resources.getResourceEntryName(R.drawable.walking),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit"

        ),
        Task(
            name = "Running",
            ava = context.resources.getResourceEntryName(R.drawable.running),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit"
        ),
        Task(
            name = "Yoga",
            ava = context.resources.getResourceEntryName(R.drawable.yoga_1),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit"
        ),
        Task(
            name = "Full-body stretching",
            ava = context.resources.getResourceEntryName(R.drawable.archery),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit"
        ),
        Task(
            name = "Swimming",
            ava = context.resources.getResourceEntryName(R.drawable.swimming),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit"
        ),
        Task(
            name = "Cycling",
            ava = context.resources.getResourceEntryName(R.drawable.cycling),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit"
        ),
        Task(
            name = "Hit the gym",
            ava = context.resources.getResourceEntryName(R.drawable.gym),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit"
        ),

    )


    private val financeTasks = mutableListOf<Task>(
        Task(
            name = "Create shopping list",
            ava = context.resources.getResourceEntryName(R.drawable.cart),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance"

        ),
        Task(
            name = "Set saving goal",
            ava = context.resources.getResourceEntryName(R.drawable.piggy_bank),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance"
        ),
        Task(
            name = "Track expense",
            ava = context.resources.getResourceEntryName(R.drawable.lot_of_cash),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance"
        ),
        Task(
            name = "Pay the bills",
            ava = context.resources.getResourceEntryName(R.drawable.hand_cash),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance"
        ),
        Task(
            name = "Check investment",
            ava = context.resources.getResourceEntryName(R.drawable.bank),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance"
        ),
        Task(
            name = "Review subscriptions",
            ava = context.resources.getResourceEntryName(R.drawable.wallet),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance"
        ),

        )


    private val selfimproveTasks = mutableListOf<Task>(
        Task(
            name = "Try something new",
            ava = context.resources.getResourceEntryName(R.drawable.sparks),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements"

        ),
        Task(
            name = "Practice a new skill",
            ava = context.resources.getResourceEntryName(R.drawable.golf),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements"
        ),
        Task(
            name = "Volunteer",
            ava = context.resources.getResourceEntryName(R.drawable.medal),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements"
        ),
        Task(
            name = "Listen to podcast",
            ava = context.resources.getResourceEntryName(R.drawable.lullaby),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements"
        ),
        Task(
            name = "Read",
            ava = context.resources.getResourceEntryName(R.drawable.open_book_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements"
        ),
        Task(
            name = "Reflect on my day",
            ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements"
        ),
        )


    private val hitgymTasks = mutableListOf<Task>(
        Task(
            name = "Wam-up",
            ava = context.resources.getResourceEntryName(R.drawable.sea_waves),
            tag = "Hit the Gym"

        ),
        Task(
            name = "Burpees",
            ava = context.resources.getResourceEntryName(R.drawable.sea_and_sun),
            tag = "Hit the Gym"

        ),
        Task(
            name = "Pushups",
            ava = context.resources.getResourceEntryName(R.drawable.trophy),
            tag = "Hit the Gym"

        ),
        Task(
            name = "Core & Leg workout",
            ava = context.resources.getResourceEntryName(R.drawable.soccer_ball),
            tag = "Hit the Gym"

        ),
        Task(
            name = "Upper body tranning",
            ava = context.resources.getResourceEntryName(R.drawable.waist),
            tag = "Hit the Gym"

        ),
        Task(
            name = "Core strength tranning",
            ava = context.resources.getResourceEntryName(R.drawable.treadmill),
            tag = "Hit the Gym"

        ),

        Task(
            name = "Squats",
            ava = context.resources.getResourceEntryName(R.drawable.hourglass),
            tag = "Hit the Gym"

        ),
    )
    private var localCollection: MutableList<HabitCollection> = mutableListOf(
        HabitCollection(
            context.resources.getResourceEntryName( R.drawable.bg_collection1), "Nighttime", nightTimeTasks, R.color.blue
        ),
        HabitCollection(
            context.resources.getResourceEntryName(R.drawable.bg_collection2), "Morning Routine", morningTasks, R.color.pink
        ),
        HabitCollection(
            context.resources.getResourceEntryName( R.drawable.bg_collection3), "Heathy lifestyle", healthyTasks, R.color.orange
        ),
        HabitCollection(
            context.resources.getResourceEntryName(R.drawable.bg_collection4), "Daily Chores", dailyChoreTasks, R.color.green
        ),
        HabitCollection(
            context.resources.getResourceEntryName(R.drawable.bg_collection5), "Stress relief", stressreliefTasks, R.color.purple
        ),
        HabitCollection(
            context.resources.getResourceEntryName(R.drawable.bg_collection6), "Study", studyTasks, R.color.blue
        ),
        HabitCollection(
            context.resources.getResourceEntryName(R.drawable.bg_collection7), "Personal finance", financeTasks, R.color.pink
        ),
        HabitCollection(
            context.resources.getResourceEntryName(R.drawable.bg_collection8), "Staying fit", stayingfitTasks, R.color.orange
        ),
        HabitCollection(
            context.resources.getResourceEntryName( R.drawable.bg_collection9), "Hit the Gym", hitgymTasks, R.color.green
        ),
        HabitCollection(
            context.resources.getResourceEntryName( R.drawable.bg_collection10), "Self-improvement", selfimproveTasks, R.color.purple
        ),
    )

    override fun getListLocalCollection(): List<HabitCollection> {
        return localCollection
    }

    override fun getListRemoteCollection(): List<HabitCollection> {
        return mutableListOf()
    }
}