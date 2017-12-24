package com.travel.phuc.trung.tlcn.tlcn.schedules

class ScheduleEventData {
    constructor()

    // lich ke hoach
    constructor(key : String, title: String, detail: String, ngayBD: String, ngayKT: String) {
        this.key = key
        this.title = title
        this.detail = detail
        this.ngayBD = ngayBD
        this.ngayKT = ngayKT
    }

    var key : String = ""
    var title : String = ""
    var detail : String = ""

    var ngayBD : String = ""
    var ngayKT : String = ""
}