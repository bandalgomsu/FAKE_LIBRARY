package com.library.app.common.llm

enum class BookKeyword(val label: String) {
    마법("마법"),
    드래곤("드래곤"),
    이세계("이세계"),
    기사단("기사단"),
    마법학교("마법학교"),
    전설의_무기("전설의 무기"),
    운명("운명"),
    신화("신화"),
    대륙_전쟁("대륙 전쟁"),
    인공지능("인공지능"),
    시간여행("시간여행"),
    우주선("우주선"),
    클론("클론"),
    평행세계("평행세계"),
    유전자_조작("유전자 조작"),
    지구_종말("지구 종말"),
    가상현실("가상현실"),
    첫사랑("첫사랑"),
    썸("썸"),
    고백("고백"),
    재회("재회"),
    연애_계약("연애 계약"),
    금지된_사랑("금지된 사랑"),
    삼각관계("삼각관계"),
    츤데레("츤데레"),
    운명적인_만남("운명적인 만남"),
    용의자("용의자"),
    단서("단서"),
    반전("반전"),
    추격("추격"),
    경찰("경찰"),
    증거("증거"),
    트릭("트릭"),
    목격자("목격자"),
    타임_리밋("타임 리밋"),
    명탐정("명탐정"),
    닫힌_방("닫힌 방"),
    밀실("밀실"),
    실종("실종"),
    죽음의_편지("죽음의 편지"),
    과거의_비밀("과거의 비밀"),
    유산("유산"),
    금지된_방("금지된 방"),
    귀신("귀신"),
    악령("악령"),
    저주("저주"),
    빙의("빙의"),
    제물("제물"),
    꿈속의_괴물("꿈속의 괴물"),
    불_꺼진_집("불 꺼진 집"),
    끊긴_전화("끊긴 전화"),
    우정("우정"),
    졸업("졸업"),
    첫_연애("첫 연애"),
    진로("진로"),
    가족과_갈등("가족과 갈등"),
    교내_사건("교내 사건"),
    학교폭력("학교폭력"),
    사춘기("사춘기"),
    자아찾기("자아찾기"),
    위로("위로"),
    나답게("나답게"),
    혼자만의_시간("혼자만의 시간"),
    감정기록("감정기록"),
    관계의_온도("관계의 온도"),
    치유("치유"),
    소소한_행복("소소한 행복"),
    목표_달성("목표 달성"),
    시간_관리("시간 관리"),
    마인드셋("마인드셋"),
    돈("돈"),
    루틴("루틴"),
    퍼스널_브랜딩("퍼스널 브랜딩"),
    스타트업("스타트업"),
    창업("창업"),
    제국("제국"),
    혁명("혁명"),
    전쟁("전쟁"),
    황제("황제"),
    장군("장군"),
    민초의_삶("민초의 삶"),
    국난_극복("국난 극복"),
    문화유산("문화유산"),
    삶의_의미("삶의 의미"),
    자아("자아"),
    공감("공감"),
    무의식("무의식"),
    불안("불안"),
    행복론("행복론"),
    선택("선택"),
    내면의_대화("내면의 대화"),
    상실("상실"),
    사랑과_이별("사랑과 이별"),
    가족의_서사("가족의 서사"),
    일상의_비극("일상의 비극"),
    관계의_균열("관계의 균열"),
    기억("기억"),
    노년("노년"),
    계절("계절"),
    그리움("그리움"),
    바람("바람"),
    노을("노을"),
    사랑의_파편("사랑의 파편"),
    짧은_말_긴_울림("짧은 말 긴 울림"),
    검객("검객"),
    내공("내공"),
    사파("사파"),
    무림("무림"),
    절정고수("절정고수"),
    비급("비급"),
    복수("복수"),
    혈전("혈전"),
    강호("강호"),
    회귀("회귀"),
    주인공_성장("주인공 성장"),
    먼치킨("먼치킨"),
    숨겨진_혈통("숨겨진 혈통"),
    후회남("후회남"),
    여주각성("여주각성"),
    랭킹("랭킹"),
    파티("파티"),
    로그라이크("로그라이크"),
    스킬트리("스킬트리"),
    전설템("전설템"),
    PvP("PvP"),
    레이드("레이드"),
    버그("버그"),
    승부욕("승부욕"),
    노력("노력"),
    팀워크("팀워크"),
    리더십("리더십"),
    부상("부상"),
    역전승("역전승"),
    멘탈("멘탈"),
    경기장의_함성("경기장의 함성"),
    낯선_도시("낯선 도시"),
    문화충격("문화충격"),
    여행자의_시선("여행자의 시선"),
    로컬푸드("로컬푸드"),
    길_위에서("길 위에서"),
    모험("모험"),
    프로그래머("프로그래머"),
    코딩("코딩"),
    직장인("직장인");

    companion object {
        fun getKeywordsByOrders(orders: List<Int>): List<String> {
            val keywords = BookKeyword.entries

            return orders.map { order ->
                keywords[order].label
            }.toList()
        }
    }

}
