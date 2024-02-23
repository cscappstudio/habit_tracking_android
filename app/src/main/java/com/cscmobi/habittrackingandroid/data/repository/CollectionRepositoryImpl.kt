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
            name = context.getString(R.string.practice_affirmations),
            ava = context.resources.getResourceEntryName(R.drawable.emoji_sing_right_note),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.practice_visualization),
            ava = context.resources.getResourceEntryName(R.drawable.media_image_list),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.meditate),
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.make_the_bed),
            ava = context.resources.getResourceEntryName(R.drawable.bed),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.brush_teeth),
            ava = context.resources.getResourceEntryName(R.drawable.fluent_mdl2_teeth),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.drink_water),
            ava = context.resources.getResourceEntryName(R.drawable.glass_half),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.eat_breakfast),
            ava = context.resources.getResourceEntryName(R.drawable.bread_slice),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.pray),
            ava = context.resources.getResourceEntryName(R.drawable.flower),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ), Task(
            name = context.getString(R.string.take_a_shower),
            ava = context.resources.getResourceEntryName(R.drawable.bathroom),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.take_vitamins),
            ava = context.resources.getResourceEntryName(R.drawable.apple),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Morning Routine",
            notBelongDefaultCollection = false
        ),
    )


    private val nightTimeTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.warm_and_cozy_drink),
                ava = context.resources.getResourceEntryName(R.drawable.bbq),
                repeate = TaskRepeat(true,"daily",1),
                tag = "Nighttime",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.skincare),
            ava = context.resources.getResourceEntryName(R.drawable.soap),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.reflect_on_my_day),
            ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.stretch),
            ava = context.resources.getResourceEntryName(R.drawable.stretching),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.aroma_candle),
            ava = context.resources.getResourceEntryName(R.drawable.fire_flame),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.meditate),
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_2),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Nighttime",
            notBelongDefaultCollection = false
        ),
    )

    private val dailyChoreTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.make_bed),
            ava = context.resources.getResourceEntryName(R.drawable.crib),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.laundry),
            ava = context.resources.getResourceEntryName(R.drawable.washing_machine),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.sweep_or_vacuum),
            ava = context.resources.getResourceEntryName(R.drawable.pacman),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.task_out_trash),
            ava = context.resources.getResourceEntryName(R.drawable.bin_half),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.meal_prep),
            ava = context.resources.getResourceEntryName(R.drawable.cutlery),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.water_plants),
            ava = context.resources.getResourceEntryName(R.drawable.soil_alt),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Daily Chores",
            notBelongDefaultCollection = false
        ),
    )


    private val healthyTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.fruit_and_veggies),
            ava = context.resources.getResourceEntryName(R.drawable.frame_48),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.drink_water),
            ava = context.resources.getResourceEntryName(R.drawable.glass_half_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.limit_sugar),
            ava = context.resources.getResourceEntryName(R.drawable.half_cookie),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.no_alcohol),
            ava = context.resources.getResourceEntryName(R.drawable.glass_empty),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.get_enough_sleep),
            ava = context.resources.getResourceEntryName(R.drawable.small_lamp),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.take_vitamins),
            ava = context.resources.getResourceEntryName(R.drawable.apple_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Healthy Lifestyle",
            notBelongDefaultCollection = false
        ),
    )


    private val studyTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.set_goals),
            ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.review_material),
            ava = context.resources.getResourceEntryName(R.drawable.bookmark_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.take_note),
            ava = context.resources.getResourceEntryName(R.drawable.favourite_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.brainstorming),
            ava = context.resources.getResourceEntryName(R.drawable.brain),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.prioritize_tasks),
            ava = context.resources.getResourceEntryName(R.drawable.rocket),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.study_groups),
            ava = context.resources.getResourceEntryName(R.drawable.graduation_cap),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Study Groups",
            notBelongDefaultCollection = false
        ),
    )


    private val stressreliefTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.set_goals),
            ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.drink_tea),
            ava = context.resources.getResourceEntryName(R.drawable.coffee_cup),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.meditate),
            ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_1),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.watch_movie),
            ava = context.resources.getResourceEntryName(R.drawable.modern_tv),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.music_on),
            ava = context.resources.getResourceEntryName(R.drawable.headset),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.stretch),
            ava = context.resources.getResourceEntryName(R.drawable.stretching_1),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.yoga),
            ava = context.resources.getResourceEntryName(R.drawable.yoga),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.paint_or_draw),
            ava = context.resources.getResourceEntryName(R.drawable.palette),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.read),
            ava = context.resources.getResourceEntryName(R.drawable.open_book),
            repeate = TaskRepeat(true,"daily",1),
            tag = "Stress Relief",
            notBelongDefaultCollection = false
        ),
    )


    private val stayingfitTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.jogging),
            ava = context.resources.getResourceEntryName(R.drawable.walking),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.running),
            ava = context.resources.getResourceEntryName(R.drawable.running),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.yoga),
            ava = context.resources.getResourceEntryName(R.drawable.yoga_1),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.full_body_stretching),
            ava = context.resources.getResourceEntryName(R.drawable.archery),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.swimming),
            ava = context.resources.getResourceEntryName(R.drawable.swimming),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.cycling),
            ava = context.resources.getResourceEntryName(R.drawable.cycling),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.hit_the_gym),
            ava = context.resources.getResourceEntryName(R.drawable.gym),
            repeate = TaskRepeat(true,type = "weekly",1, listOf(2,3)),
            tag = "Get Fit",
            notBelongDefaultCollection = false
        ),

    )


    private val financeTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.create_shopping_list),
            ava = context.resources.getResourceEntryName(R.drawable.cart),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.set_saving_goal),
            ava = context.resources.getResourceEntryName(R.drawable.piggy_bank),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.track_expense),
            ava = context.resources.getResourceEntryName(R.drawable.lot_of_cash),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.pay_the_bills),
            ava = context.resources.getResourceEntryName(R.drawable.hand_cash),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.check_investment),
            ava = context.resources.getResourceEntryName(R.drawable.bank),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.review_subscriptions),
            ava = context.resources.getResourceEntryName(R.drawable.wallet),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Finance",
            notBelongDefaultCollection = false
        ),

        )


    private val selfimproveTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.try_something_new),
            ava = context.resources.getResourceEntryName(R.drawable.sparks),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.practice_a_new_skill),
            ava = context.resources.getResourceEntryName(R.drawable.golf),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.volunteer),
            ava = context.resources.getResourceEntryName(R.drawable.medal),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.listen_to_podcast),
            ava = context.resources.getResourceEntryName(R.drawable.lullaby),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.read),
            ava = context.resources.getResourceEntryName(R.drawable.open_book_1),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        Task(
            name = context.getString(R.string.reflect_on_my_day),
            ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
            repeate = TaskRepeat(true,type = "daily",1),
            tag = "Self-Improvements",
            notBelongDefaultCollection = false
        ),
        )


    private val hitgymTasks = mutableListOf<Task>(
        Task(
            name = context.getString(R.string.wam_up),
            ava = context.resources.getResourceEntryName(R.drawable.sea_waves),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.burpees),
            ava = context.resources.getResourceEntryName(R.drawable.sea_and_sun),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.pushups),
            ava = context.resources.getResourceEntryName(R.drawable.trophy),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.core_leg_workout),
            ava = context.resources.getResourceEntryName(R.drawable.soccer_ball),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.upper_body_tranning),
            ava = context.resources.getResourceEntryName(R.drawable.waist),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
        Task(
            name = context.getString(R.string.core_strength_tranning),
            ava = context.resources.getResourceEntryName(R.drawable.treadmill),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),

        Task(
            name = context.getString(R.string.squats),
            ava = context.resources.getResourceEntryName(R.drawable.hourglass),
            tag = "Hit the Gym",
            notBelongDefaultCollection = false

        ),
    )
    private var localCollection: MutableList<HabitCollection> = mutableListOf(
        HabitCollection(
           image = context.resources.getResourceEntryName( R.drawable.bg_collection1), name = context.getString(
                R.string.nighttime
            ), task = nightTimeTasks, resColorBg =  R.color.blue
        ),
        HabitCollection(
            image = context.resources.getResourceEntryName(R.drawable.bg_collection2), name = context.getString(
                R.string.morning_routine
            ), task =morningTasks, resColorBg =R.color.pink
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName( R.drawable.bg_collection3), name = context.getString(
                R.string.heathy_lifestyle
            ),task = healthyTasks, resColorBg =R.color.orange
        ),
        HabitCollection(
            image =   context.resources.getResourceEntryName(R.drawable.bg_collection4),name = context.getString(
                R.string.daily_chores
            ),task = dailyChoreTasks,resColorBg = R.color.green
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName(R.drawable.bg_collection5),name = context.getString(
                R.string.stress_relief
            ),task = stressreliefTasks,resColorBg = R.color.purple
        ),
        HabitCollection(
            image =   context.resources.getResourceEntryName(R.drawable.bg_collection6),name = context.getString(
                R.string.study
            ), task =studyTasks, resColorBg =R.color.blue
        ),
        HabitCollection(
            image =   context.resources.getResourceEntryName(R.drawable.bg_collection7),name = context.getString(
                R.string.personal_finance
            ), task =financeTasks,resColorBg = R.color.pink
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName(R.drawable.bg_collection8),name = context.getString(
                R.string.staying_fit
            ),task = stayingfitTasks,resColorBg = R.color.orange
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName( R.drawable.bg_collection9), name =context.getString(R.string.hit_the_gym), task =hitgymTasks, resColorBg =R.color.green
        ),
        HabitCollection(
            image =  context.resources.getResourceEntryName( R.drawable.bg_collection10),name = context.getString(
                R.string.self_improvement
            ), task =selfimproveTasks, resColorBg = R.color.purple
        ),
    )

    override suspend fun getListLocalCollection(): Flow<List<HabitCollection>> {
        return flow { emit(localCollection) }
    }

    override fun getListRemoteCollection(): List<HabitCollection> {
        return mutableListOf()
    }
}