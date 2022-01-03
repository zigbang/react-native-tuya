# TuyaSdkBridge SDK Reference

TuyaSdkBridges는 투야 기기 등록을 위해 필요한 기능에 집중하여 제공됨.<br>
react-native-tuya 모듈을 적절히 사용하고 있음.<br>
현재는 TuyaSdkBridge는 패키지와 별도로 위치하고 있음.<br>

## `init`

`init(isShowDebugLog: boolean, pnu: string, dong: string, ho: string, user: string, host: string): Promise<string>`<br><br>
TuyaSdkBridge 사용을 위한 초기화 수행<br><br>

- **Parameters**

| 프로퍼티       | 타입    | 설명                                                   |
| -------------- | ------- | ------------------------------------------------------ |
| isShowDebugLog | boolean | 디버그 정보 활용 여부 (true - 콘솔 출력 정보가 많아짐) |
| pnu            | string  | 기기 등록시 활용되는 건물 정보                         |
| dong           | string  | 기기 등록시 활용되는 동 정보                           |
| ho             | string  | 기기 등록시 활용되는 호 정보                           |
| user           | string  | 기기 등록시 활용되는 사용자 이름                       |
| host           | string  | Tuya의 익명 로그인을 위한 서버 주소                    |

- **Return**

| 타입              | 설명                       |
| ----------------- | -------------------------- |
| Promise< string > | 초기화 함수 정상 수행 여부 |

- **Sample**

```
	TuyaSdkBridge.init(false, "1929129192919", "101동", "301호", "서진우", "10.200.18.212")
```

## `startSearchWiredGW`

`startSearchWiredGW(callback: (gw_id: string, product_id: string) => void): boolean`<br><br>
유선 게이트웨이 탐색 시작<br>
callback 함수를 통한 gw_id와 product_id 수집 필요<br><br>

- **Parameters**

| 프로퍼티 | 타입     | 설명                                                                    |
| -------- | -------- | ----------------------------------------------------------------------- |
| callback | function | 게이트웨이 정보(gw_id, product_id)를 사용자에게 전달하기 위한 콜백 함수 |

- **Return**

| 타입    | 설명                                                                       |
| ------- | -------------------------------------------------------------------------- |
| boolean | 함수 수행 정상 여부, startSearchWiredGW 함수 중복 호출등의 경우 false 리턴 |

- **Sample**

```
let tempScannedData = []

function filterNotification(gwId: string, productId: string) {
  const result = tempScannedData.filter((tempScannedData) => {
    if (tempScannedData.productId == productId) {
      return true
    }
  })
  if (result.length == 0) {
    tempScannedData.push({ gwId: gwId, productId: productId })
  }
}

TuyaSdkBridge.startSearchWiredGW(filterNotification)
```

## `stopSearchWiredGW`

`stopSearchWiredGW(): boolean`<br><br>
유선 게이트웨이 탐색 중지 <br><br>

- **Parameters**

| 프로퍼티 | 타입 | 설명 | 필수 |
| -------- | ---- | ---- | ---- |
|          |      |      |      |

- **Return**

| 프로퍼티    | 타입    | 설명                                                                           |
| ----------- | ------- | ------------------------------------------------------------------------------ |
| ReturnValue | boolean | 함수 수행 정상 여부, startSearchWiredGW 호출 없이 해당 함수 호출 시 false 리턴 |

- **Sample**

```
setTimeout(() => {
  TuyaSdkBridge.stopSearchWiredGW()
}, (100000))

```

## `registerWiredGW`

`registerWiredGW(gw_id: string, product_id: string, timeout: number): Promise<string>`<br><br>
유선 게이트웨이 등록<br>
등록에 성공한 경우, 기기 이름에 건물/동호/등록자 정보를 표시함 (직방가드 메타 정보)<br><br>

- **Parameters**

| 프로퍼티   | 타입   | 설명                                     |
| ---------- | ------ | ---------------------------------------- |
| gw_id      | string | 등록하고자 하는 게이트웨이 아이디        |
| product_id | string | 등록하고자 하는 게이트웨이의 제품 아이디 |
| timeout    | number | 함수 응답에 대한 최대 대기 시간          |

- **Return**

| 프로퍼티 | 타입   | 설명                                                                                     |
| -------- | ------ | ---------------------------------------------------------------------------------------- |
| Promise  | string | 등록 성공 시, 등록된 디바이스 세부 정보 출력. 실패 시, 유형에 따라 서로 다른 메시지 출력 |

- **Return examples**

| 유형                   | 값                                                                                                                              |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| 등록 성공 시           | [참고 링크](https://www.notion.so/zigbang/Tuya-SDK-SampleApp-22f1038ad9f1405385a9e5d01b83cbfc#0acd728c310d4226990f4792621e7d42) |
| 이미 등록 진행 중일 시 | registerWiredGW is started                                                                                                      |
| 타임아웃 발생 시       | Timeout                                                                                                                         |

- **Sample**

```
TuyaSdkBridge.registerWiredGw({
  "eb9a55f0d10d1c9a11luux",
  "keyyj3fy8x98arty",
  90
}).then(
  (okRes: any) => {
    debugText("Ok Res")
    assignedGwId = okRes.devId
    console.log(assignedGwId)
    console.log(okRes)
  },
  (errRes) => {
    debugText("Ng Res")
    console.log(errRes)
  }
)
```

## `startRegisterZigbeeSubDevice`

`startRegisterZigbeeSubDevice(gw_id: string, timeout: number, callback: (result: any) => void): Promise<boolean>`<br><br>
게이트웨이 하위에 지그비 기기 등록 시작<br>
검색기능은 없고, 조건에 맞으면 바로 등록 됨. callback 함수를 통해 기기의 세부 정보 습득.<br><br>

- **Parameters**

| 프로퍼티 | 타입     | 설명                                                        |
| -------- | -------- | ----------------------------------------------------------- |
| gw_id    | string   | 지그비 기기를 연결하고자 하는 특정 게이트웨이에 대한 아이디 |
| timeout  | number   | 함수 응답에 대한 최대 대기 시간                             |
| callback | function | 기기 등록시, 사용자에게 전달하기 위한 콜백 함수             |

- **Return**

| 프로퍼티 | 타입    | 설명                |
| -------- | ------- | ------------------- |
| Promise  | boolean | 함수 수행 정상 여부 |

- **Sample**

```
const registerNotificationForSubDevice = async (result: any) => {
  console.log(result)
}

TuyaSdkBridge.startRegisterZigbeeSubDevice(
  "eb9a55f0d10d1c9a11luux",
  100,
  registerNotificationForSubDevice
).then(
  (okRes: boolean) => {
    debugText("Ok Res")
  },
  (errRes) => {
    debugText("Ng Res")
  }
)
```

## `stopRegisterZigbeeSubDevice`

`stopRegisterZigbeeSubDevice(): boolean`<br><br>
게이트웨이 하위에 지그비 기기 등록 중지<br><br>

- **Parameters**

| 프로퍼티 | 타입 | 설명 |
| -------- | ---- | ---- |
|          |      |      |

- **Return**

| 타입    | 설명                                                                    |
| ------- | ----------------------------------------------------------------------- |
| boolean | 함수 수행 정상 여부, startRegisterZigbeeSubDevice 미호출 시, false 리턴 |

- **Sample**

```
setTimeout(() => {
  TuyaSdkBridge.stopRegisterZigbeeSubDevice()
}, 100000)
```

## `removeDevice`

`removeDevice(devId: TuyaNative.RemoveDeviceParams, reset: boolean): Promise<boolean>`<br><br>
투야 기기 삭제<br><br>

- **Parameters**

| 프로퍼티 | 타입    | 설명                                   |
| -------- | ------- | -------------------------------------- |
| devId    | string  | 삭제하고자 하는 기기의 디바이스 아이디 |
| reset    | boolean | 공장초기화 여부                        |

- **Return**

| 프로퍼티 | 타입    | 설명                |
| -------- | ------- | ------------------- |
| Promise  | boolean | 함수 수행 정상 여부 |

- **Sample**

```
const res = await TuyaSdkBridge.removeDevice({ devId: "ebc2e6912054fa2a03ws5h" }, true)
console.log(res)
```
