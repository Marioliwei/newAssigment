package com.example.newassigment

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Users(var email:String,
            var fullName: String="",
            var username: String="",
            var userState: String=""){
    constructor():this("","","",""){}
}
