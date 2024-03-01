package com.cscmobi.habittrackingandroid.utils

import com.cscmobi.habittrackingandroid.data.model.CheckList
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.data.model.Goal
import com.cscmobi.habittrackingandroid.data.model.RemindTask
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.FakeData.createDate
import com.cscmobi.habittrackingandroid.utils.FakeData.generateRandomTasks
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit
import java.util.Calendar
import java.util.Random
import java.util.concurrent.TimeUnit

//fun main() {
//    val startDate = createDate(2024, 1, 20)
//    val endDate = createDate(2024, 2, 5)
//
//    val randomTasks = generateRandomTasks(startDate, endDate, 15)
//
//    for (task in randomTasks) {
//        println(task)
//    }
//}

object FakeData {



    fun createDate(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.timeInMillis
    }

    fun generateRandomTasks(startDate: Long, endDate: Long, numberOfTasks: Long): List<Task> {
        val random = Random()
        val tasks = ArrayList<Task>()

        for (i in 0..numberOfTasks-1) {
            val t = getRandomType(random)
            val task = Task(
                id = i,
                name = "Task $i",
                color = getRandomColor(random),
                ava = getRandomAva(random),
                note = getRandomString(random, 20),
                tag = getRandomString(random, 10),
                collection = getRandomString(random, 15),
                pauseDate = getRandomDateBetween(startDate, endDate, random),
                pause = random.nextInt(2),
                challenge = getRandomString(random, 10),
                goal = Goal(/* Initialize Goal object with appropriate values */),
                repeate  = TaskRepeat(
                    isOn = random.nextBoolean(),
                    type = t,
                    frequency = random.nextInt(5) + 1,
                    days = getRandomDaysList(random,t )
                ),
                startDate = getRandomDateBetween(startDate, endDate, random),
                endDate = EndDate(
                    isOpen = random.nextBoolean(),
                    endDate = getRandomDateBetween(startDate, endDate, random)
                ),
                remind = RemindTask(
                    isOpen = random.nextBoolean(),
                    hour = random.nextInt(24),
                    minute = random.nextInt(60),
                    unit = if (random.nextBoolean()) "AM" else "PM"
                ),
                checklist = generateRandomChecklist(random)
            )

            tasks.add(task)
        }

        return tasks
    }

    fun generateRandomHistory(startDate: Long, endDate: Long, randomTasks: List<Task>): List<History> {
        val random = Random()
        val numberOfDays = ChronoUnit.DAYS.between(
            Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate(),
            Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate()
        ).toInt()
        return (0 until numberOfDays).map { dayOffset ->
            val currentDate = startDate + TimeUnit.DAYS.toMillis(dayOffset.toLong())
            val tasksInDay = randomTasks.map { task ->
                val progress = random.nextInt(101) // Random progress between 0 and 100
                val progressGoal = random.nextInt(21) // Assuming a default target of 1 if goal is null
                TaskInDay(task.id, progress, progressGoal)
            }
            History(0, currentDate, tasksInDay)
        }
    }

    fun getRandomColor(random: Random): String {
        val letters = "0123456789ABCDEF"
        val color = StringBuilder("#")
        repeat(6) {
            color.append(letters[random.nextInt(16)])
        }
        return color.toString()
    }

    fun getRandomAva(random: Random): String {
        val avaOptions = listOf("ic_item_collection1", "ic_item_collection2", "ic_item_collection3")
        return avaOptions[random.nextInt(avaOptions.size)]
    }

    fun getRandomString(random: Random, length: Int): String {
        val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        return (1..length)
            .map { letters[random.nextInt(letters.length)] }
            .joinToString("")
    }

    fun getRandomDateBetween(startDate: Long, endDate: Long, random: Random): Long {
        require(startDate <= endDate) { "Invalid date range" }

        val diff = endDate - startDate + 1
        return startDate + (random.nextDouble() * diff).toLong()
    }


    fun getRandomDaysList(random: Random): List<Int> {
        return List(random.nextInt(7) + 1) { random.nextInt(31) + 1 }
    }

    fun generateRandomChecklist(random: Random): List<CheckList> {
        val checklist = ArrayList<CheckList>()
        repeat(random.nextInt(5) + 1) {
            checklist.add(CheckList(status = random.nextBoolean(), title = getRandomString(random, 10)))
        }
        return checklist
    }

    fun getRandomDaysList(random: Random, type: String): List<Int>? {
        val maxValue = when (type) {
            "weekly" -> 7
            "monthly" -> 31
            else -> return null
        }

        val numberOfDays = random.nextInt(maxValue) + 1
        val uniqueDays = (1..maxValue).shuffled(random).take(numberOfDays)

        return uniqueDays.toList()
    }

    fun getRandomType(random: Random): String {
        val types = listOf("daily", "weekly", "monthly")
        return types[random.nextInt(types.size)]
    }



}