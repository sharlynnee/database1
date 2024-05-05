package com.example.wazitoecommerce.ui.theme.screens.parents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.wazitoecommerce.data.ParentViewModel
import com.example.wazitoecommerce.models.Parent
import com.example.wazitoecommerce.ui.theme.WazitoECommerceTheme

@Composable
fun ViewParentsScreen(navController:NavHostController) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        var context = LocalContext.current
        var parentRepository = ParentViewModel(navController, context)


        val emptyParentState = remember { mutableStateOf(Parent("","","","","")) }
        var emptyParentsListState = remember { mutableStateListOf<Parent>() }

        var parents = parentRepository.allParents(emptyParentState, emptyParentsListState)


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "All parents",
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = Color.Red)

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(){
                items(parents){
                    ParentItem(
                        name = it.name,
                        age = it.age,
                        description = it.description,
                        id = it.id,
                        navController = navController,
                        parentRepository = parentRepository,
                        parentImage = it.imageUrl
                    )
                }

            }
        }
    }
}


@Composable
fun ParentItem(name:String, age:String, description:String, id:String,
                navController:NavHostController,
                parentRepository:ParentViewModel, parentImage:String) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = name)
        Text(text = age)
        Text(text = description)
        Image(
            painter = rememberAsyncImagePainter(parentImage),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Button(onClick = {
            parentRepository.deleteParent(id)
        }) {
            Text(text = "Delete")
        }
        Button(onClick = {
            //navController.navigate(ROUTE_UPDATE_PRODUCTS+"/$id")
        }) {
            Text(text = "Update")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ViewParentsScreenPreview(){
    WazitoECommerceTheme {
        ViewParentsScreen(navController = rememberNavController())
    }
}