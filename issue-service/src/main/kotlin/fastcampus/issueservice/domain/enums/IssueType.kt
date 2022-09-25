package fastcampus.issueservice.domain.enums

enum class IssueType {
    BUG, TASK;

    companion object {
//        fun of (type:String) = valueOf(type.uppercase())
        operator fun invoke (type:String) = valueOf(type.uppercase())
    }
}

fun main() {
//    val type = IssueType.of("BUG")
    val type = IssueType("BUG")
    println(type == IssueType.BUG)
    println(type)
}