export default ({
  state: {
    status: "matching", //playing表示对战
    socket:null,
    opponent_username:"",
    opponent_photo:"",
    gameMap: null,
    a_id: 0,
    a_sx: 0,
    a_sy: 0,
    b_id: 0,
    b_sx: 0,
    b_sy: 0,
    gameObject:null,
    loser:"none",
  },
  getters: {
  },
  mutations: {
    updateSocket(state, socket){
        state.socket = socket
    },
    updateOpponent(state, opponent){
        state.opponent_username = opponent.username
        state.opponent_photo = opponent.photo
    },
    updateStatus(state, status){
        state.status = status
    },
    updateGameInfo(state, GameInfo){
      state.gameMap = GameInfo.gameMap
      state.a_id = GameInfo.a_id
      state.a_sx = GameInfo.a_sx
      state.a_sy = GameInfo.a_sy
      state.b_id = GameInfo.b_id
      state.b_sx = GameInfo.b_sx
      state.b_sy = GameInfo.b_sy
  },
    updateGameObject(state, gameObject){
      state.gameObject = gameObject
  },
  updateLoser(state, loser){
    state.loser = loser
},
  },
  actions: {
  },
  modules: {
  }
})
