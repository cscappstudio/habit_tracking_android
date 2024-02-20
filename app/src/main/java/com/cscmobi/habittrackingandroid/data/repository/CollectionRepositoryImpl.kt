package com.cscmobi.habittrackingandroid.data.repository

import android.content.Context
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CollectionRepositoryImpl(val context: Context) : CollectionRepository {
    private val morningTasks = mutableListOf<Task>(
        Task(
            name = "Practice affirmations",
            ava = context.resources.getResourceEntryName(R.drawable.emoji_sing_right_note),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.media_image_list),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Meditate",
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Make the bed",
            ava = context.resources.getResourceEntryName(R.drawable.bed),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Brush teeth",
            ava = context.resources.getResourceEntryName(R.drawable.fluent_mdl2_teeth),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Drink water",
            ava = context.resources.getResourceEntryName(R.drawable.glass_half),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Eat breakfast",
            ava = context.resources.getResourceEntryName(R.drawable.bread_slice),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Pray",
            ava = context.resources.getResourceEntryName(R.drawable.flower),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ), Task(
            name = "Take a shower",
            ava = context.resources.getResourceEntryName(R.drawable.bathroom),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Take vitamins",
            ava = context.resources.getResourceEntryName(R.drawable.apple),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
    )


    private val nightTimeTasks = mutableListOf<Task>(
        Task(
            name = "Warm and cozy drink",
                ava = context.resources.getResourceEntryName(R.drawable.bbq),
                repeate = TaskRepeat(true,"daily",1),
                tag = "Nighttime",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Skincare",
            ava = context.resources.getResourceEntryName(R.drawable.soap),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Reflect on my day",
            ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Stretch",
            ava = context.resources.getResourceEntryName(R.drawable.stretching),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Aroma candle",
            ava = context.resources.getResourceEntryName(R.drawable.fire_flame),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Meditate",
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_2),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
    )

    private val dailyChoreTasks = mutableListOf<Task>(
        Task(
            name = "Make bed",
            ava = context.resources.getResourceEntryName(R.drawable.crib),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Laundry",
            ava = context.resources.getResourceEntryName(R.drawable.washing_machine),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Sweep or Vacuum",
            ava = context.resources.getResourceEntryName(R.drawable.pacman),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Task out trash",
            ava = context.resources.getResourceEntryName(R.drawable.bin_half),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Meal prep",
            ava = context.resources.getResourceEntryName(R.drawable.cutlery),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Water plants",
            ava = context.resources.getResourceEntryName(R.drawable.soil_alt),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
    )


    private val healthyTasks = mutableListOf<Task>(
        Task(
            name = "Fruit and veggies",
            ava = context.resources.getResourceEntryName(R.drawable.frame_48),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Drink water",
            ava = context.resources.getResourceEntryName(R.drawable.glass_half_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Limit sugar",
            ava = context.resources.getResourceEntryName(R.drawable.half_cookie),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "No alcohol",
            ava = context.resources.getResourceEntryName(R.drawable.glass_empty),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Get enough sleep",
            ava = context.resources.getResourceEntryName(R.drawable.small_lamp),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Take vitamins",
            ava = context.resources.getResourceEntryName(R.drawable.apple_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
    )


    private val studyTasks = mutableListOf<Task>(
        Task(
            name = "Set goals",
            ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Review material",
            ava = context.resources.getResourceEntryName(R.drawable.bookmark_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Take note",
            ava = context.resources.getResourceEntryName(R.drawable.favourite_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Brainstorming",
            ava = context.resources.getResourceEntryName(R.drawable.brain),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Prioritize tasks",
            ava = context.resources.getResourceEntryName(R.drawable.rocket),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Study groups",
            ava = context.resources.getResourceEntryName(R.drawable.graduation_cap),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
    )


    private val stressreliefTasks = mutableListOf<Task>(
        Task(
            name = "Set goals",
            ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Drink tea",
            ava = context.resources.getResourceEntryName(R.drawable.coffee_cup),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Meditate",
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_1),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Watch movie",
            ava = context.resources.getResourceEntryName(R.drawable.modern_tv),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Music on",
            ava = context.resources.getResourceEntryName(R.drawable.headset),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Stretch",
            ava = context.resources.getResourceEntryName(R.drawable.stretching_1),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Yoga",
            ava = context.resources.getResourceEntryName(R.drawable.yoga),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Paint or draw",
            ava = context.resources.getResourceEntryName(R.drawable.palette),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Read",
            ava = context.resources.getResourceEntryName(R.drawable.open_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
    )


    private val stayingfitTasks = mutableListOf<Task>(
        Task(
            name = "Jogging",
            ava = context.resources.getResourceEntryName(R.drawable.walking),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Running",
            ava = context.resources.getResourceEntryName(R.drawable.running),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Yoga",
            ava = context.resources.getResourceEntryName(R.drawable.yoga_1),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Full-body stretching",
            ava = context.resources.getResourceEntryName(R.drawable.archery),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Swimming",
            ava = context.resources.getResourceEntryName(R.drawable.swimming),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Cycling",
            ava = context.resources.getResourceEntryName(R.drawable.cycling),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Hit the gym",
            ava = context.resources.getResourceEntryName(R.drawable.gym),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),

    )


    private val financeTasks = mutableListOf<Task>(
        Task(
            name = "Create shopping list",
            ava = context.resources.getResourceEntryName(R.drawable.cart),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Set saving goal",
            ava = context.resources.getResourceEntryName(R.drawable.piggy_bank),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Track expense",
            ava = context.resources.getResourceEntryName(R.drawable.lot_of_cash),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Pay the bills",
            ava = context.resources.getResourceEntryName(R.drawable.hand_cash),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Check investment",
            ava = context.resources.getResourceEntryName(R.drawable.bank),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Review subscriptions",
            ava = context.resources.getResourceEntryName(R.drawable.wallet),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),

        )


    private val selfimproveTasks = mutableListOf<Task>(
        Task(
            name = "Try something new",
            ava = context.resources.getResourceEntryName(R.drawable.sparks),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Practice a new skill",
            ava = context.resources.getResourceEntryName(R.drawable.golf),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Volunteer",
            ava = context.resources.getResourceEntryName(R.drawable.medal),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Listen to podcast",
            ava = context.resources.getResourceEntryName(R.drawable.lullaby),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Read",
            ava = context.resources.getResourceEntryName(R.drawable.open_book_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = "Reflect on my day",
            ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        )


    private val hitgymTasks = mutableListOf<Task>(
        Task(
            name = "Wam-up",
            ava = context.resources.getResourceEntryName(R.drawable.sea_waves),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Burpees",
            ava = context.resources.getResourceEntryName(R.drawable.sea_and_sun),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Pushups",
            ava = context.resources.getResourceEntryName(R.drawable.trophy),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Core & Leg workout",
            ava = context.resources.getResourceEntryName(R.drawable.soccer_ball),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Upper body tranning",
            ava = context.resources.getResourceEntryName(R.drawable.waist),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = "Core strength tranning",
            ava = context.resources.getResourceEntryName(R.drawable.treadmill),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),

        Task(
            name = "Squats",
            ava = context.resources.getResourceEntryName(R.drawable.hourglass),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
    )
    private var localCollection: MutableList<HabitCollection> = mutableListOf(
        HabitCollection(
           image = context.resources.getResourceEntryName( R.drawable.bg_collection1), name ="Nighttime", task = nightTimeTasks, resColorBg =  R.color.blue
        ),
        HabitCollection(
            image = context.resources.getResourceEntryName(R.drawable.bg_collection2), name ="Morning Routine", task =morningTasks, resColorBg =R.color.pink
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName( R.drawable.bg_collection3), name ="Heathy lifestyle",task = healthyTasks, resColorBg =R.color.orange
        ),
        HabitCollection(
            image =   context.resources.getResourceEntryName(R.drawable.bg_collection4),name = "Daily Chores",task = dailyChoreTasks,resColorBg = R.color.green
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName(R.drawable.bg_collection5),name = "Stress relief",task = stressreliefTasks,resColorBg = R.color.purple
        ),
        HabitCollection(
            image =   context.resources.getResourceEntryName(R.drawable.bg_collection6),name = "Study", task =studyTasks, resColorBg =R.color.blue
        ),
        HabitCollection(
            image =   context.resources.getResourceEntryName(R.drawable.bg_collection7),name = "Personal finance", task =financeTasks,resColorBg = R.color.pink
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName(R.drawable.bg_collection8),name = "Staying fit",task = stayingfitTasks,resColorBg = R.color.orange
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName( R.drawable.bg_collection9), name ="Hit the Gym", task =hitgymTasks, resColorBg =R.color.green
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName( R.drawable.bg_collection10),name = "Self-improvement", task =selfimproveTasks, resColorBg = R.color.purple
        ),
    )

    override suspend fun getListLocalCollection(): Flow<List<HabitCollection>> {
        return flow { emit(localCollection) }
    }

    override fun getListRemoteCollection(): List<HabitCollection> {
        return mutableListOf()
    }
}