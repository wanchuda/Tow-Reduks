package wanchuda.reduks.common.separator

enum class DbState {
    NONE,
    REQUESTING,
    SAVE,
    SUCCESS,
    FAIL,
    UNCHANGED
}