## 추가 및 수정된 API

|API|설명|인자|응답|
|------|---|---|---|
|initWiredGwActivator|유선 게이트웨이 연결을 위한 API|homeId, ssid, time||
|newGwSubDevActivator|등록되어 있는 지그비 게이트웨이 하위에 디바이스 연결을 위한 API|deviceId, time|success, failed|
|resetDevice|등록되어 있는 디바이스 공장 초기화를 위한 API|deviceId|success, failed|
|inviteHomeMember|다른 계정을 동일한 홈에 등록하기 위해 초대 코드를 생성하는 API|homeId||
|joinHomeMember|초대 코드를 사용하여 특정 홈에 조인하기 위한 API|invitationCode||
