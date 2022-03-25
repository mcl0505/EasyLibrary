package com.easy.lib_util.city

import android.content.Context
import com.easy.lib_util.LogUtil
import com.easy.lib_util.app.FileUtil
import com.google.gson.Gson
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：CityData
 * 创建时间：2020/8/21
 * 功能描述：
 */
object CityData {
    private val TAG = this.javaClass.simpleName

    interface CityDataCallBack {
        fun onSuccess(infoBean: AddressInfoBean?)
    }

    var mList = ArrayList<City>()

    /**
     * 根据地址信息获取城市信息
     * @param context
     * @param city
     * @param callBack
     */
    fun getCity(
        context: Context,
        city: String,
        callBack: CityDataCallBack
    ) {
        val JsonData = getJson(context, "province.json") //获取assets目录下的json文件数据
        if (mList.size > 0) {
        } else {
            mList = parseData(JsonData) //用Gson 转成实体
        }
        for (i in mList.indices) {
            for (c in mList[i].cityList.indices) {
                if (city.contains(mList[i].name + mList[i].cityList[c].name)) {
                    val infoBean = AddressInfoBean()
                    infoBean.province = mList[i].name
                    infoBean.city = mList[i].cityList[c].name
                    infoBean.details = city.substring(
                        mList[i].name.length + mList[i].cityList[c].name.length
                    )

                    callBack.onSuccess(infoBean)
                    return
                } else {

                }
            }
        }
    }

    fun initJsonData(context: Context) { //解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         */
        val JsonData = FileUtil.readAssetsFileContent("province.json") //获取assets目录下的json文件数据

        LogUtil.d(JsonData)

        if (mList.size > 0) {
        } else {
            mList = parseData(JsonData!!) //用Gson 转成实体
        }

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        for (i in mList.indices) { //遍历省份
            val cityList =
                ArrayList<String>() //该省的城市列表（第二级）
            val province_AreaList =
                ArrayList<ArrayList<String>>() //该省的所有地区列表（第三极）
            for (c in mList[i].cityList.indices) { //遍历该省份的所有城市
                val cityName =
                    mList[i].cityList[c].name
                cityList.add(cityName) //添加城市
                val city_AreaList =
                    ArrayList<String>() //该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                    || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                city_AreaList.add("");
            } else {
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
            }*/city_AreaList.addAll(
                    mList[i].cityList[c].area
                )
                province_AreaList.add(city_AreaList) //添加该省所有地区数据
            }
        }
    }

    fun getJson(context: Context, fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName!!)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    private fun parseData(result: String): ArrayList<City> { //Gson 解析
        val detail = ArrayList<City>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity =
                    gson.fromJson(data.optJSONObject(i).toString(), City::class.java)
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return detail
    }
}
