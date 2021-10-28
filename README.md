## 추가 및 수정된 API

| API                   | 설명                                                                    | 인자                                       | 응답            |
| --------------------- | ----------------------------------------------------------------------- | ------------------------------------------ | --------------- |
| initWiredGwActivator  | 유선 게이트웨이 연결을 위한 API                                         | homeId, ssid, time                         |                 |
| newGwSubDevActivator  | 등록되어 있는 지그비 게이트웨이 하위에 디바이스 연결을 위한 API         | deviceId, time                             | success, failed |
| resetDevice           | 등록되어 있는 디바이스 공장 초기화를 위한 API                           | deviceId                                   | success, failed |
| inviteHomeMember      | 다른 계정을 동일한 홈에 등록하기 위해 초대 코드를 생성하는 API          | homeId                                     |                 |
| joinHomeMember        | 초대 코드를 사용하여 특정 홈에 조인하기 위한 API                        | invitationCode                             |                 |
| loginWithEmailAndCode | 이메일로 받은 verification code를 통해 첫 이메일 로그인을 하기 위한 API | countryCode, email, code(이메일 인증 코드) |                 |
| sendEmailBindingCode  | 이메일 정보가 없는 계정에 이메일 정보를 등록하기 위한 API               | countryCode, email                         |                 |
| loginByGoogle         | 써드파티 로그인 중 구글 로그인을 위한 API                               | countryCode, code(idToken)                 |                 |
