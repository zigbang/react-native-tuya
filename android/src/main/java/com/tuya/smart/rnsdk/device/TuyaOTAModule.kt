package com.tuya.smart.rnsdk.device

import com.facebook.react.bridge.*
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IGetOtaInfoCallback
import com.tuya.smart.android.device.bean.UpgradeInfoBean
import com.tuya.smart.rnsdk.utils.*
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.sdk.api.IOtaListener
import com.tuya.smart.sdk.api.ITuyaOta
import com.tuya.smart.sdk.bean.OTAErrorMessageBean


class TuyaOTAModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
       return "TuyaOTAModule"
    }

    var iTuyaOta:ITuyaOta?=null
    /* 获取固件升级信息 */
    @ReactMethod
    fun getOtaInfo(params: ReadableMap,promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            getIoat(params.getString(DEVID) as String).getOtaInfo(object :IGetOtaInfoCallback{
                override fun onSuccess(list: List<UpgradeInfoBean>) {
                    promise.resolve(TuyaReactUtils.parseToWritableArray(
                            JsonUtils.toJsonArray(list)))
                }

               override fun onFailure(code: String, error: String){
                    promise.reject(code,error)
                }
            })
        }
    }
    /* 设置升级状态回调 */
    @ReactMethod
    fun startOta(params: ReadableMap) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            iTuyaOta = getIoat(params.getString(DEVID) as String)
            iTuyaOta?.setOtaListener(object : IOtaListener {
                override fun onSuccess(otaType: Int) {
                    var map=Arguments.createMap();
                    map.putInt("otaType",otaType)
                    map.putString("type","onSuccess")
                    BridgeUtils.hardwareUpgradeListener(reactApplicationContext,map,params.getString(DEVID) as String)
                }

                override fun onTimeout(otaType: Int) {
                  //
                }

                override fun onFailureWithText(otaType: Int, code: String, messageBean: OTAErrorMessageBean) {
                  //
                }

                override fun onFailure(otaType: Int, code: String, error: String) {
                    var map=Arguments.createMap();
                    map.putInt("otaType",otaType)
                    map.putString("error",error)
                    map.putString("code",code)
                    map.putString("type","onFailure")
                    BridgeUtils.hardwareUpgradeListener(reactApplicationContext,map,params.getString(DEVID) as String)
                }

                override fun onProgress(otaType: Int, progress: Int) {
                    var map=Arguments.createMap();
                    map.putInt("otaType",otaType)
                    map.putInt("progress",progress)
                    map.putString("type","onProgress")
                    BridgeUtils.hardwareUpgradeListener(reactApplicationContext,map,params.getString(DEVID) as String)
                }
            })
            iTuyaOta?.startOta()
        }
    }
    @ReactMethod
    fun onDestroy(){
        iTuyaOta?.onDestroy()
    }
    fun getIoat(devId:String): ITuyaOta {
        return TuyaHomeSdk.newOTAInstance(devId)
    }
}
