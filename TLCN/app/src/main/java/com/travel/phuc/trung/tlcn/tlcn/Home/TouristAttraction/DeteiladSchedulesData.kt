package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction


// lớp chứa dữ liệu lịch trình
class DeteiladSchedulesData {

    constructor()

    // lich ke hoach
    constructor(detail : String, key: String, ngayBD: String, ngayKT: String, title: String) {
        this.detail = detail
        this.key = key
        this.ngayBD = ngayBD
        this.ngayKT = ngayKT
        this.title = title
    }

    var detail : String = ""
    var key : String = ""
    var ngayBD : String = ""
    var ngayKT : String = ""
    var title : String = ""
}