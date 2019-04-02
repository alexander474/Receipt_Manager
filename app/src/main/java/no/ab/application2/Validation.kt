package no.ab.application2

class Validation{


    fun validate(input: String, minLen: Int): Boolean{
        if(input.length>=minLen) return true
        return false
    }

    fun validate(input: String): Boolean{
        if(input.isNotEmpty()) return true
        return false
    }

    fun validate(input: List<String>, minLen: Int): Boolean{
        var passed = true
        input.forEach { if(it.length>=minLen) passed = true else passed = false }
        return passed
    }

    fun validate(input: List<String>): Boolean{
        var passed = true
        input.forEach { if(it.isNotEmpty()) passed = true else passed = false }
        return passed
    }
}