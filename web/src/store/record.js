export default ({
  state: {
    isRecord: false,
    a_step:"",
    b_step:"",
    record_loser:""
  },
  getters: {
  },
  mutations: {
    updateIsRecord(state, isRecord){
        state.isRecord = isRecord
    },
    updateSteps(state, data){
        state.a_step = data.a_step
        state.b_step = data.b_step
    },
    updateRecordLoser(state,loser){
        state.record_loser = loser
    }
  },
  actions: {
  },
  modules: {
  }
})
