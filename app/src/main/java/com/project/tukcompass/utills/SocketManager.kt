

@singletone
class SocketManager @Inject constructor() {

  private var socket : Socket? = null

  private val _connected = MutableStateFlow(false)
  val connected: StateFlow<Boolean> = _connected 

  private val _incommingMessages = MutableSharedFlow<MessageModel>(extraBits)
  val incommingMessages : SharedFlow<MessageModel> = _incommingMessages

  fun connect ( baseUrl : String, token: String){
    if ( socket?.connected() == true) return

    val opts = IO.options().apply{
      reconnection = true
      reconnectionAttempts = Int.MAX_VALUE 
      reconnectionDelay = 1000
      query = "token=$token"
    }

    socket = IO.socket(baseUrl, opts)

    socket?.on(socket.EVENT_CONNECT){
      _connected.tyrEmit(true)
    }?.on(socket.EVENT_DISCONNECT){
      _connected.tryEmit(false)
    }?.on("new_message"){ message ->
      (message.firstorNull() as? org.json.JSONOBJECT)?.let{ json ->
         val msg = MessageModel(
            id = json.optString("id", null),
            chatId = json.getString("chatID"),
            senderId = json.getString("senderID"),
            message = json.getString("message"),
            mediaUrl = json.optString("mediaUrl", null),
            mediaType = json.optString("mediaType", null),
            createdAt = json.getString("createdAt")
          )

         _incominMessages.tryEmit(msg)
      }
      socket?.connect()
    }
    fun disconnect() {
        socket?.disconnect()
        socket?.off()
        socket = null
        _connected.tryEmit(false)
    }

    fun joinChat(chatId: String) {
        socket?.emit("join_chat", chatId)
    }

    fun sendMessage(out: OutgoingMessage) {
        val obj = org.json.JSONObject().apply {
            put("chatID", out.chatId)
            put("message", out.message)
            if (out.mediaUrl != null) put("mediaUrl", out.mediaUrl)
            if (out.mediaType != null) put("mediaType", out.mediaType)
        }
        socket?.emit("send_message", obj)
    }
    




  
}

