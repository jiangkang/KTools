package com.jiangkang.design.recyclerview

data class AreaModel(
        val provinceList: MutableList<Province>
) {
    data class Province(
            val name: String,
            val cityList: MutableList<City>
    ) {
        data class City(
                val name: String
        )
    }
}

class ExpandableAreaModel {
    companion object {
        const val Province = 1
        const val City = 2
    }

    private var province: AreaModel.Province? = null
    private var city: AreaModel.Province.City? = null
}
