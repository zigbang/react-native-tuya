package com.tuya.smart.rnsdk.activator

import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.facebook.react.bridge.*
import com.tuya.smart.android.common.utils.WiFiUtil

import com.tuya.smart.android.ble.api.TyBleScanResponse
import com.tuya.smart.android.ble.api.ScanType
import com.tuya.smart.android.ble.api.ScanType.SINGLE
import com.tuya.smart.android.ble.api.ScanDeviceBean
import com.tuya.smart.android.ble.api.LeScanSetting
import com.tuya.smart.android.ble.api.LeScanSetting.Builder

import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.builder.ActivatorBuilder
import com.tuya.smart.home.sdk.builder.TuyaGwActivatorBuilder
import com.tuya.smart.rnsdk.utils.*
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.PASSWORD
import com.tuya.smart.rnsdk.utils.Constant.SSID
import com.tuya.smart.rnsdk.utils.Constant.TIME

import com.tuya.smart.sdk.api.ITuyaActivator
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener
import com.tuya.smart.sdk.api.IMultiModeActivatorListener
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.bean.MultiModeActivatorBean
import com.tuya.smart.sdk.enums.ActivatorModelEnum
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken
import com.tuya.smart.home.sdk.api.ITuyaGwSearcher
import com.tuya.smart.home.sdk.api.IGwSearchListener
import com.tuya.smart.home.sdk.builder.TuyaGwSubDevActivatorBuilder
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.rnsdk.utils.Constant.TYPE
import com.tuya.smart.android.hardware.bean.HgwBean


data class ResultEvent(val Result: String, val Var1: Any, val Var2: Any)

val SearchedDevices : MutableMap<String, HgwBean> = mutableMapOf()

class TuyaActivatorModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  var mITuyaActivator: ITuyaActivator? = null
  var mTuyaGWActivator: ITuyaActivator? = null
  override fun getName(): String {
    return "TuyaActivatorModule"
  }

  @ReactMethod
  fun startBluetoothScan(promise: Promise) {
    TuyaHomeSdk.getBleOperator().startLeScan(60000, ScanType.SINGLE
    ) { bean -> promise.resolve(TuyaReactUtils.parseToWritableMap(bean)) };
  }

  @ReactMethod
  fun stopBluetoothScan() {
    TuyaHomeSdk.getBleOperator().stopLeScan();
  }


  @ReactMethod
  fun initBluetoothDualModeActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, SSID, PASSWORD), params)) {

      TuyaHomeSdk.getBleOperator().startLeScan(60000, ScanType.SINGLE
      ) { bean ->
        params.getDouble(HOMEID).toLong().let {
          TuyaHomeSdk.getActivatorInstance()
            .getActivatorToken(it, object : ITuyaActivatorGetToken {
              override fun onSuccess(token: String) {
                val multiModeActivatorBean = MultiModeActivatorBean();
                multiModeActivatorBean.ssid = params.getString(SSID);
                multiModeActivatorBean.pwd = params.getString(PASSWORD);

                multiModeActivatorBean.uuid = bean.getUuid();
                multiModeActivatorBean.deviceType = bean.getDeviceType();
                multiModeActivatorBean.mac = bean.getMac();
                multiModeActivatorBean.address = bean.getAddress();


                multiModeActivatorBean.homeId = params.getDouble(HOMEID).toLong();
                multiModeActivatorBean.token = token;
                multiModeActivatorBean.timeout = 180000;
                multiModeActivatorBean.phase1Timeout = 60000;

                TuyaHomeSdk.getActivator().newMultiModeActivator()
                  .startActivator(multiModeActivatorBean, object : IMultiModeActivatorListener {
                    override fun onSuccess(bean: DeviceBean) {
                      promise.resolve(TuyaReactUtils.parseToWritableMap(bean));
                    }

                    override fun onFailure(code: Int, msg: String?, handle: Any?) {
                      promise.reject(code.toString(), msg);
                    }
                  });
              }

              override fun onFailure(s: String, s1: String) {
                promise.reject(s, s1);
              }
            })
        }
      };
    }
  }

  @ReactMethod
  fun getCurrentWifi(params: ReadableMap, successCallback: Callback, errorCallback: Callback) {
    successCallback.invoke(WiFiUtil.getCurrentSSID(reactApplicationContext.applicationContext));
  }

  @ReactMethod
  fun openNetworkSettings(params: ReadableMap) {
    val currentActivity = currentActivity
    if (currentActivity == null) {
      return
    }
    try {
      currentActivity.startActivity(Intent(Settings.ACTION_SETTINGS))
    } catch (e: Exception) {
  }

  @ReactMethod
  fun initActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, SSID, PASSWORD, TIME, TYPE), params)) {
      TuyaHomeSdk.getActivatorInstance().getActivatorToken(params.getDouble(HOMEID)?.toLong(), object : ITuyaActivatorGetToken {
        override fun onSuccess(token: String) {
          mITuyaActivator = TuyaHomeSdk.getActivatorInstance().newActivator(ActivatorBuilder()
            .setSsid(params.getString(SSID))
            .setContext(reactApplicationContext.applicationContext)
            .setPassword(params.getString(PASSWORD))
            .setActivatorModel(ActivatorModelEnum.valueOf(params.getString(TYPE) as String))
            .setTimeOut(params.getInt(TIME).toLong())
            .setToken(token).setListener(getITuyaSmartActivatorListener(promise)))
          mITuyaActivator?.start()
        }
        override fun onFailure(s: String, s1: String) {
          promise.reject(s, s1)
        }
      }
    }
  }

  @ReactMethod
  fun initWiredGwActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, TIME), params)){
      Log.d("initWiredGwActivator", " Initialized")
      //TuyaHomeSdk.getActivatorInstance().newTuyaGwActivator().newSearcher()
      val mTuyaGwSearcher : ITuyaGwSearcher = TuyaHomeSdk.getActivatorInstance().newTuyaGwActivator().newSearcher()

      mTuyaGwSearcher.registerGwSearchListener(object : IGwSearchListener {
        override fun onDevFind(hgwBean:HgwBean) {
          Log.d("initWiredGwActivator", " hgwBean:" + hgwBean)
          TuyaHomeSdk.getActivatorInstance().getActivatorToken(params.getDouble(HOMEID).toLong(),object : ITuyaActivatorGetToken {
            override fun onSuccess(token: String) {
              Log.d("initWiredGwActivator", " token:" + token)
              var mITuyaActivator : ITuyaActivator = TuyaHomeSdk.getActivatorInstance().newGwActivator( TuyaGwActivatorBuilder()
                .setToken(token)
                .setTimeOut(params.getInt(TIME).toLong())
                .setContext(reactApplicationContext.applicationContext)
                .setListener( getITuyaSmartActivatorListenerForGW(promise)))
              mITuyaActivator.start()
            }
            override fun onFailure(errorCode: String, errorMsg: String) {
              promise.reject(errorCode, errorMsg)
            }
          })
        }
      })
    }
  }

  @ReactMethod
  fun StartSearcingGwDevice() {
    Log.d("[Log]TuyaActivatorModule.kt", "게이트웨이 서치 시작")
    val mTuyaGwSearcher : ITuyaGwSearcher = TuyaHomeSdk.getActivatorInstance().newTuyaGwActivator().newSearcher()
    
    mTuyaGwSearcher.registerGwSearchListener(object : IGwSearchListener { 
      override fun onDevFind(hgwBean:HgwBean) {
        Log.d("[Log]TuyaActivatorModule.kt", "onDevFind")
        if( SearchedDevices.containsKey(hgwBean.gwId) == false ){
          Log.d("[Log]TuyaActivatorModule.kt", "SearchedDevices.containsKey(hgwBean.gwId)")

          SearchedDevices.put(hgwBean.gwId, hgwBean)
        }

        BridgeUtils.foundGateway(reactApplicationContext, TuyaReactUtils.parseToWritableMap(hgwBean))
      }
    })
  }
    
  @ReactMethod
  fun InitSearchedGwDevice(params: ReadableMap, promise: Promise) {
    Log.d("initWiredGwActivator1", " 11")

    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, TIME, DEVID), params)){
      var DeviceID = params.getString(DEVID)
      Log.d("initWiredGwActivator2", " 22")

      if( SearchedDevices.containsKey(DeviceID) )
      {
        Log.d("initWiredGwActivator3", params.getDouble(HOMEID).toLong().toString())
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(params.getDouble(HOMEID).toLong(),object : ITuyaActivatorGetToken {
          override fun onSuccess(token: String) {
            Log.d("initWiredGwActivator", " token:" + token)
            var mITuyaActivator : ITuyaActivator = TuyaHomeSdk.getActivatorInstance().newGwActivator( TuyaGwActivatorBuilder()
              .setToken(token)
              .setTimeOut(params.getInt(TIME).toLong())
              .setContext(reactApplicationContext.applicationContext)
              .setHgwBean(SearchedDevices[DeviceID])
              .setListener(getITuyaSmartActivatorListenerForGW(promise)))
            mITuyaActivator.start()
          }
          override fun onFailure(errorCode: String, errorMsg: String) {
            Log.d("initWiredGwActivatorError",errorCode)

            promise.reject(errorCode, errorMsg)
          }
        })
      }
      else
      {

      }            
    }
  }


    /**
     * ZigBee 하위 장치 네트워크 구성은 ZigBee 게이트웨이 장치 클라우드가 온라인 상태이고 
     * 하위 장치가 네트워크 구성 상태인 경우에만 시작할 수 있습니다.
     */
    @ReactMethod
    fun newGwSubDevActivator(params: ReadableMap,promise: Promise) {
      if (ReactParamsCheck.checkParams(arrayOf(DEVID, TIME), params)){
        val builder = TuyaGwSubDevActivatorBuilder()
          //게이트웨이 ID 설정
          .setDevId(params.getString(DEVID))
          //네트워크 시간 초과 설정
          .setTimeOut(params.getInt(TIME).toLong())
          .setListener(object : ITuyaSmartActivatorListener {
            override fun onError(var1: String, var2: String) {
              promise.reject(var1,var2)
            }

            /**
              * 장치가 성공적으로 네트워크에 연결되어 있고 장치가 온라인 상태이면(전화를 직접 제어할 수 있음) 다음을 사용할 수 있습니다.
              */
            override fun onActiveSuccess(var1: DeviceBean) {
              promise.resolve(TuyaReactUtils.parseToWritableMap(var1))
            }

            /**
              * device_find 기기 검색
              * device_bind_success 장치가 성공적으로 바인딩되었지만 아직 온라인 상태가 아닙니다. 현재 장치가 오프라인 상태이므로 제어할 수 없습니다.
              */
            override fun onStep(var1: String, var2: Any) {
              promise.reject(var1,"")
            }
          })

        mTuyaGWActivator = TuyaHomeSdk.getActivatorInstance().newGwSubDevActivator(builder)
        mTuyaGWActivator?.start()
      }
    }

    /**
     * ZigBee 하위 장치 네트워크 구성은 ZigBee 게이트웨이 장치 클라우드가 온라인 상태이고 
     * 하위 장치가 네트워크 구성 상태인 경우에만 시작할 수 있습니다.
     */
    @ReactMethod
    fun StartGwSubDevActivator(params: ReadableMap, promise: Promise) {
      var ErrorOccur :Boolean = true
      if (ReactParamsCheck.checkParams(arrayOf(DEVID, TIME), params)){
        val builder = TuyaGwSubDevActivatorBuilder()
          //게이트웨이 ID 설정
          .setDevId(params.getString(DEVID))
          //네트워크 시간 초과 설정
          .setTimeOut(params.getInt(TIME).toLong())
          .setListener(object : ITuyaSmartActivatorListener {
            /**
            1001 네트워크 오류
            1002 배포 네트워크 장치의 활성화 인터페이스 호출이 실패했으며 인터페이스 호출이 실패했습니다.
            1003 네트워크 배포 장치의 활성화에 실패했으며 장치를 찾을 수 없습니다.
            1004 토큰 획득 실패
            1005 장치가 온라인 상태가 아닙니다.
            1006 네트워크 배포 시간 초과
            */
            override fun onError(var1: String, var2: String) {
              var PassParam = ResultEvent ("onError",var1,var2)
              BridgeUtils.resultSubDevice(reactApplicationContext, TuyaReactUtils.parseToWritableMap(PassParam))
            }

            /**
            * 장치가 성공적으로 네트워크에 연결되어 있고 장치가 온라인 상태이면(전화를 직접 제어할 수 있음) 다음을 사용할 수 있습니다.
            */
            override fun onActiveSuccess(var1: DeviceBean) {
              var PassParam = ResultEvent ("onActiveSuccess",var1,"none")
              BridgeUtils.resultSubDevice(reactApplicationContext, TuyaReactUtils.parseToWritableMap(PassParam))
            }

            /**
            * device_find 기기 검색
            device_bind_success 장치가 성공적으로 바인딩되었지만 아직 온라인 상태가 아닙니다. 현재 장치가 오프라인 상태이므로 제어할 수 없습니다.
            */
            override fun onStep(var1: String, var2: Any) {
              // IOS 일관성을 유지하기 위한 onStep 없음
              var PassParam = ResultEvent ("onStep",var1,var2)
              BridgeUtils.resultSubDevice(reactApplicationContext, TuyaReactUtils.parseToWritableMap(PassParam))
            }
          })

          mTuyaGWActivator = TuyaHomeSdk.getActivatorInstance().newGwSubDevActivator(builder)
          mTuyaGWActivator?.start()
          ErrorOccur = false
        }

        if( ErrorOccur ) {
            promise.reject("NG")
        }
        else {
            promise.resolve("OK")
        }
      })
    }

  }

  /**
   * ZigBee子设备配网需要ZigBee网关设备云在线的情况下才能发起,且子设备处于配网状态。
   */
  @ReactMethod
  fun newGwSubDevActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(DEVID, TIME), params)) {
      val builder = TuyaGwSubDevActivatorBuilder()
        //设置网关ID
        .setDevId(params.getString(DEVID))
        //设置配网超时时间
        .setTimeOut(params.getInt(TIME).toLong())
        .setListener(object : ITuyaSmartActivatorListener {
          override fun onError(var1: String, var2: String) {
            promise.reject(var1, var2)
          }

          /**
           * 设备配网成功,且设备上线（手机可以直接控制），可以通过
           */
          override fun onActiveSuccess(var1: DeviceBean) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(var1))
          }

          /**
           * device_find 发现设备
          device_bind_success 设备绑定成功，但还未上线，此时设备处于离线状态，无法控制设备。
           */
          override fun onStep(var1: String, var2: Any) {
            // promise.reject(var1,"")
          }
        })

      mTuyaGWActivator = TuyaHomeSdk.getActivatorInstance().newGwSubDevActivator(builder)
    }
  }

  @ReactMethod
  fun stopConfig() {
    mITuyaActivator?.stop()
    mTuyaGWActivator?.stop()
  }

  @ReactMethod
  fun onDestory() {
    mITuyaActivator?.onDestroy()
    mTuyaGWActivator?.onDestroy()
  }

  fun getITuyaSmartActivatorListener(promise: Promise): ITuyaSmartActivatorListener {
    return object : ITuyaSmartActivatorListener {
      /**
       * 1001        网络错误
      1002        配网设备激活接口调用失败，接口调用不成功
      1003        配网设备激活失败，设备找不到。
      1004        token 获取失败
      1005        设备没有上线
      1006        配网超时
       */
      override fun onError(var1: String, var2: String) {
        promise.reject(var1, var2)
      }

      /**
       * 设备配网成功,且设备上线（手机可以直接控制），可以通过
       */
      override fun onActiveSuccess(var1: DeviceBean) {
        promise.resolve(TuyaReactUtils.parseToWritableMap(var1))
      }

      /**
       * device_find 发现设备
      device_bind_success 设备绑定成功，但还未上线，此时设备处于离线状态，无法控制设备。
       */
      override fun onStep(var1: String, var2: Any) {
        // IOS 没有onStep保持一致
        //promise.reject(var1,"")
      }
    }
  }

  @ReactMethod
  fun getITuyaSmartActivatorListenerForSubDevice() : ITuyaSmartActivatorListener {
    return object : ITuyaSmartActivatorListener {
      /**
      1001 네트워크 오류
      1002 배포 네트워크 장치의 활성화 인터페이스 호출이 실패했으며 인터페이스 호출이 실패했습니다.
      1003 네트워크 배포 장치의 활성화에 실패했으며 장치를 찾을 수 없습니다.
      1004 토큰 획득 실패
      1005 장치가 온라인 상태가 아닙니다.
      1006 네트워크 배포 시간 초과
        */
      override fun onError(var1: String, var2: String) {
        var PassParam = ResultEvent ("onError",var1,var2)
        BridgeUtils.resultSubDevice(reactApplicationContext, TuyaReactUtils.parseToWritableMap(PassParam))
      }

      /**
        * 장치가 성공적으로 네트워크에 연결되어 있고 장치가 온라인 상태이면(전화를 직접 제어할 수 있음) 다음을 사용할 수 있습니다.
        */
      override fun onActiveSuccess(var1: DeviceBean) {
        var PassParam = ResultEvent ("onActiveSuccess",var1,"none")
        BridgeUtils.resultSubDevice(reactApplicationContext, TuyaReactUtils.parseToWritableMap(PassParam))
      }

      /**
        * device_find 기기 검색
          device_bind_success 장치가 성공적으로 바인딩되었지만 아직 온라인 상태가 아닙니다. 현재 장치가 오프라인 상태이므로 제어할 수 없습니다.
        */
      override fun onStep(var1: String, var2: Any) {
        // IOS 일관성을 유지하기 위한 onStep 없음
        var PassParam = ResultEvent ("onStep",var1,var2)
        BridgeUtils.resultSubDevice(reactApplicationContext, TuyaReactUtils.parseToWritableMap(PassParam))
      }
    }
  }

  fun getITuyaSmartActivatorListenerForGW(promise: Promise): ITuyaSmartActivatorListener {
    return object : ITuyaSmartActivatorListener {
      /**
      1001 네트워크 오류
      1002 배포 네트워크 장치의 활성화 인터페이스 호출이 실패했으며 인터페이스 호출이 실패했습니다.
      1003 네트워크 배포 장치의 활성화에 실패했으며 장치를 찾을 수 없습니다.
      1004 토큰 획득 실패
      1005 장치가 온라인 상태가 아닙니다.
      1006 네트워크 배포 시간 초과
        */
      override fun onError(var1: String, var2: String) {
        promise.reject(var1,var2)
      }

      /**
        * 장치가 성공적으로 네트워크에 연결되어 있고 장치가 온라인 상태이면(전화를 직접 제어할 수 있음) 다음을 사용할 수 있습니다.
        */
      override fun onActiveSuccess(var1: DeviceBean) {
        promise.resolve(TuyaReactUtils.parseToWritableMap(var1))
      }

      /**
        * device_find 기기 검색
          device_bind_success 장치가 성공적으로 바인딩되었지만 아직 온라인 상태가 아닙니다. 현재 장치가 오프라인 상태이므로 제어할 수 없습니다.
        */
      override fun onStep(var1: String, var2: Any) {
        // IOS 일관성을 유지하기 위한 onStep 없음
        // promise.resolve("OK")
      }
    }
  }
}
