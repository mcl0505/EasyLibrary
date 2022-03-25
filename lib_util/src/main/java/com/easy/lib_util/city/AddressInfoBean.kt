package com.easy.lib_util.city

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：AddressInfoBean
 * 创建时间：2020/8/21
 * 功能描述：   地址信息
 */
data class AddressInfoBean(
    var province: String? = null,//省
    var city: String? = null,//市
    var area: String? = null,//区
    var details: String? = null,//详细地址
    var lng: String? = null,//经度
    var lat: String? = null,//纬度
)

data class City(
    var name: String,
    var cityList: List<CityBean>
)

/**
 * name : 城市
 * area : ["东城区","西城区","崇文区","昌平区"]
 */
data class CityBean(
    var name: String,
    var area: List<String>
)