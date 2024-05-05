package com.example.wazitoecommerce.models

class Parent {
    var name:String = ""
    var age:String = ""
    var description:String = ""
    var imageUrl:String = ""
    var id:String = ""

    constructor(name: String, age: String, description: String, imageUrl: String, id: String) {
        this.name = name
        this.age = age
        this.description = description
        this.imageUrl = imageUrl
        this.id = id
    }

    constructor()
}