package com.cscmobi.habittrackingandroid.data.repository

import android.content.Context
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import com.cscmobi.habittrackingandroid.utils.Utils.resetResouceConfig

class CollectionRepositoryImpl(val context: Context) : CollectionRepository {
    private var localCollection = listOf<HabitCollection>()
    fun init() {

        val context = context.resetResouceConfig()
         val morningTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.practice_affirmations).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.emoji_sing_right_note),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.practice_visualization).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.media_image_list),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.meditate).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.make_the_bed).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.bed),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.brush_teeth).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.fluent_mdl2_teeth),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.drink_water).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.glass_half),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.eat_breakfast).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.bread_slice),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.pray).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.flower),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.take_a_shower).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.bathroom),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.take_vitamins).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.apple),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Morning Routine",
                notBelongDefaultCollection = false
            ),
        )


         val nightTimeTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.warm_and_cozy_drink).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.bbq),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Nighttime",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.skincare).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.soap),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Nighttime",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.reflect_on_my_day).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Nighttime",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.stretch).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.stretching),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Nighttime",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.aroma_candle).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.fire_flame),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Nighttime",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.meditate).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_2),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Nighttime",
                notBelongDefaultCollection = false
            ),
        )


         val dailyChoreTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.make_bed).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.crib),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Daily Chores",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.laundry).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.washing_machine),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Daily Chores",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.sweep_or_vacuum).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.pacman),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Daily Chores",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.task_out_trash).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.bin_half),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Daily Chores",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.meal_prep).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.cutlery),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Daily Chores",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.water_plants).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.soil_alt),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Daily Chores",
                notBelongDefaultCollection = false
            ),
        )


         val healthyTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.fruit_and_veggies).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.frame_48),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Healthy Lifestyle",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.drink_water).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.glass_half_1),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Healthy Lifestyle",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.limit_sugar).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.half_cookie),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Healthy Lifestyle",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.no_alcohol).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.glass_empty),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Healthy Lifestyle",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.get_enough_sleep).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.small_lamp),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Healthy Lifestyle",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.take_vitamins).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.apple_1),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Healthy Lifestyle",
                notBelongDefaultCollection = false
            ),
        )


         val studyTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.set_goals).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Study Groups",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.review_material).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.bookmark_book),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Study Groups",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.take_note).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.favourite_book),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Study Groups",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.brainstorming).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.brain),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Study Groups",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.prioritize_tasks).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.rocket),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Study Groups",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.study_groups).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.graduation_cap),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Study Groups",
                notBelongDefaultCollection = false
            ),
        )


         val stressreliefTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.set_goals).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.leaderboard_star),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.drink_tea).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.coffee_cup),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.meditate).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.light_bulb_on_1),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.watch_movie).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.modern_tv),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.music_on).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.headset),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.stretch).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.stretching_1),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.yoga).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.yoga),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.paint_or_draw).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.palette),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.read).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.open_book),
                repeate = TaskRepeat(true, "daily", 1),
                tag = "Stress Relief",
                notBelongDefaultCollection = false
            ),
        )


         val stayingfitTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.jogging).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.walking),
                repeate = TaskRepeat(true, type = "weekly", 1, listOf(2, 3)),
                tag = "Get Fit",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.running).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.running),
                repeate = TaskRepeat(true, type = "weekly", 1, listOf(2, 3)),
                tag = "Get Fit",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.yoga).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.yoga_1),
                repeate = TaskRepeat(true, type = "weekly", 1, listOf(2, 3)),
                tag = "Get Fit",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.full_body_stretching).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.archery),
                repeate = TaskRepeat(true, type = "weekly", 1, listOf(2, 3)),
                tag = "Get Fit",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.swimming).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.swimming),
                repeate = TaskRepeat(true, type = "weekly", 1, listOf(2, 3)),
                tag = "Get Fit",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.cycling).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.cycling),
                repeate = TaskRepeat(true, type = "weekly", 1, listOf(2, 3)),
                tag = "Get Fit",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.hit_the_gym).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.gym),
                repeate = TaskRepeat(true, type = "weekly", 1, listOf(2, 3)),
                tag = "Get Fit",
                notBelongDefaultCollection = false
            ),

            )


         val financeTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.create_shopping_list).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.cart),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Finance",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.set_saving_goal).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.piggy_bank),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Finance",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.track_expense).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.lot_of_cash),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Finance",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.pay_the_bills).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.hand_cash),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Finance",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.check_investment).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.bank),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Finance",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.review_subscriptions).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.wallet),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Finance",
                notBelongDefaultCollection = false
            ),

            )


         val selfimproveTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.try_something_new).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.sparks),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Self-Improvements",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.practice_a_new_skill).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.golf),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Self-Improvements",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.volunteer).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.medal),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Self-Improvements",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.listen_to_podcast).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.lullaby),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Self-Improvements",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.read).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.open_book_1),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Self-Improvements",
                notBelongDefaultCollection = false
            ),
            Task(
                name = context.resources.getString(R.string.reflect_on_my_day).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.emoji_look_down),
                repeate = TaskRepeat(true, type = "daily", 1),
                tag = "Self-Improvements",
                notBelongDefaultCollection = false
            ),
        )


         val hitgymTasks = mutableListOf<Task>(
            Task(
                name = context.resources.getString(R.string.wam_up).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.sea_waves),
                tag = "Hit the Gym",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.burpees).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.sea_and_sun),
                tag = "Hit the Gym",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.pushups).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.img_trophy),
                tag = "Hit the Gym",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.core_leg_workout).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.soccer_ball),
                tag = "Hit the Gym",
                notBelongDefaultCollection = false

            ),
            Task(
                name =context.resources.getString (R.string.upper_body_tranning).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.waist),
                tag = "Hit the Gym",
                notBelongDefaultCollection = false

            ),
            Task(
                name = context.resources.getString(R.string.core_strength_tranning).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.treadmill),
                tag = "Hit the Gym",
                notBelongDefaultCollection = false

            ),

            Task(
                name = context.resources.getString(R.string.squats).toString(),
                ava = context.resources.getResourceEntryName(R.drawable.hourglass),
                tag = "Hit the Gym",
                notBelongDefaultCollection = false

            ),
        )


        localCollection  = listOf<HabitCollection>(
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection1), name = context.resources.getString(
                        R.string.nighttime
                        ).toString(), task = nightTimeTasks, resColorBg = R.color.blue
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection2), name = context.resources.getString(
                        R.string.morning_routine
                        ).toString(), task = morningTasks, resColorBg = R.color.pink
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection3),
                name = context.resources.getString(
                        R.string.heathy_lifestyle
                        ).toString(),
                task = healthyTasks,
                resColorBg = R.color.orange
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection4),
                name = context.resources.getString(
                        R.string.daily_chores
                        ).toString(),
                task = dailyChoreTasks,
                resColorBg = R.color.green
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection5),
                name = context.resources.getString(
                        R.string.stress_relief
                        ).toString(),
                task = stressreliefTasks,
                resColorBg = R.color.purple
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection6),
                name = context.resources.getString(
                        R.string.study
                        ).toString(),
                task = studyTasks,
                resColorBg = R.color.blue
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection7),
                name = context.resources.getString(
                        R.string.personal_finance
                        ).toString(),
                task = financeTasks,
                resColorBg = R.color.pink
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection8),
                name = context.resources.getString(
                        R.string.staying_fit
                        ).toString(),
                task = stayingfitTasks,
                resColorBg = R.color.orange
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection9),
                name = context.resources.getString(R.string.hit_the_gym).toString(),
                task = hitgymTasks,
                resColorBg = R.color.green
            ),
            HabitCollection(
                image = context.resources.getResourceEntryName(R.drawable.bg_collection10),
                name = context.resources.getString(
                        R.string.self_improvement
                        ).toString(),
                task = selfimproveTasks,
                resColorBg = R.color.purple
            ),
        )

    }

    override  fun getListLocalCollection(): List<HabitCollection>{
        init()
        return localCollection
    }

    override fun getListRemoteCollection(): List<HabitCollection> {
        return mutableListOf()
    }
}