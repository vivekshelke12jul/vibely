import { useState, useEffect, useRef } from 'react'
import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

function App() {
  const [screen, setScreen] = useState('login') // 'login', 'register', 'chat'
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [token, setToken] = useState(null)
  const [error, setError] = useState('')
  
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')
  const [isConnected, setIsConnected] = useState(false)
  
  const stompClientRef = useRef(null)

  // Connect to WebSocket after login
  useEffect(() => {
    if (!token) return

    const socket = new SockJS('http://localhost:8080/ws')
    const client = Stomp.over(socket)
    
    client.debug = () => {}

    client.connect(
      {},
      () => {
        setIsConnected(true)
        client.subscribe('/topic/message', (message) => {
          const msg = JSON.parse(message.body)
          setMessages(prev => [...prev, msg])
        })
      },
      (error) => {
        console.error('Connection error:', error)
        setIsConnected(false)
      }
    )

    stompClientRef.current = client

    return () => {
      if (client?.connected) {
        client.disconnect()
      }
    }
  }, [token])

  const handleAuth = async (isLogin) => {
    setError('')
    
    try {
      const response = await fetch(`http://localhost:8080/api/auth/${isLogin ? 'login' : 'register'}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      })

      const data = await response.json()

      if (!response.ok) {
        setError(data.message || 'Authentication failed')
        return
      }

      setToken(data.token)
      setScreen('chat')
    } catch (err) {
      setError('Network error. Please try again.')
    }
  }

  const sendMessage = () => {
    if (!input.trim() || !stompClientRef.current?.connected) return

    const msg = {
      username: username,
      content: input.trim()
    }

    stompClientRef.current.send('/app/sendMessage', {}, JSON.stringify(msg))
    setInput('')
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      if (screen === 'chat') {
        sendMessage()
      } else {
        handleAuth(screen === 'login')
      }
    }
  }

  const logout = () => {
    if (stompClientRef.current?.connected) {
      stompClientRef.current.disconnect()
    }
    setToken(null)
    setMessages([])
    setScreen('login')
    setUsername('')
    setPassword('')
  }

  // Login/Register Screen
  if (screen !== 'chat') {
    return (
      <div style={{ padding: '20px', maxWidth: '400px', margin: '50px auto' }}>
        <h2>{screen === 'login' ? 'Login' : 'Register'}</h2>
        
        {error && (
          <div style={{ 
            padding: '10px', 
            backgroundColor: '#fee', 
            border: '1px solid #fcc',
            marginBottom: '10px',
            borderRadius: '4px'
          }}>
            {error}
          </div>
        )}

        <div style={{ marginBottom: '10px' }}>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
            style={{ width: '100%', padding: '8px', marginBottom: '10px' }}
          />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Password"
            style={{ width: '100%', padding: '8px' }}
          />
        </div>

        <button 
          onClick={() => handleAuth(screen === 'login')}
          style={{ width: '100%', padding: '10px', marginBottom: '10px' }}
        >
          {screen === 'login' ? 'Login' : 'Register'}
        </button>

        <button 
          onClick={() => setScreen(screen === 'login' ? 'register' : 'login')}
          style={{ width: '100%', padding: '10px', background: '#eee', border: '1px solid #ccc' }}
        >
          {screen === 'login' ? 'Need an account? Register' : 'Have an account? Login'}
        </button>
      </div>
    )
  }

  // Chat Screen
  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2>Chat {isConnected ? '🟢' : '🔴'}</h2>
        <div>
          <span style={{ marginRight: '10px' }}>👤 {username}</span>
          <button onClick={logout} style={{ padding: '5px 10px' }}>Logout</button>
        </div>
      </div>
      
      <div style={{ 
        border: '1px solid #ccc', 
        height: '400px', 
        overflowY: 'auto',
        padding: '10px',
        marginBottom: '10px'
      }}>
        {messages.map((msg, idx) => (
          <div key={idx} style={{ marginBottom: '8px' }}>
            <strong>{msg.username}:</strong> {msg.content}
          </div>
        ))}
      </div>

      <div style={{ display: 'flex', gap: '10px' }}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type message..."
          disabled={!isConnected}
          style={{ flex: 1, padding: '8px' }}
        />
        <button 
          onClick={sendMessage}
          disabled={!isConnected || !input.trim()}
          style={{ padding: '8px 16px' }}
        >
          Send
        </button>
      </div>
    </div>
  )
}

export default App