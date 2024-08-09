local omgserver
omgserver = {
	constants = {
		-- Server exit codes
		ENVIRONMENT_EXIT_CODE = 1,
		TOKEN_EXIT_CODE = 2,
		CONFIG_EXIT_CODE = 3,
		API_EXIT_CODE = 4,
		WS_EXIT_CODE = 5,
		-- Server environment variables
		URL_ENVIRONMENT_VARIABLE = "OMGSERVERS_URL",
		USER_ID_ENVIRONMENT_VARIABLE = "OMGSERVERS_USER_ID",
		PASSWORD_ENVIRONMENT_VARIABLE = "OMGSERVERS_PASSWORD",
		RUNTIME_ID_ENVIRONMENT_VARIABLE = "OMGSERVERS_RUNTIME_ID",
		RUNTIME_QUALIFIER_ENVIRONMENT_VARIABLE = "OMGSERVERS_RUNTIME_QUALIFIER",
		-- Server event qualifiers
		SERVER_INITIALIZED_EVENT_QUALIFIER = "SERVER_INITIALIZED",
		COMMAND_RECEIVED_EVENT_QUALIFIER = "COMMAND_RECEIVED",
		MESSAGE_RECEIVED_EVENT_QUALIFIER = "MESSAGE_RECEIVED",
		-- Runtime qualifiers
		LOBBY_RUNTIME_QUALIFIER = "LOBBY",
		MATCH_RUNTIME_QUALIFIER = "MATCH",
		-- Service command qualifiers
		INIT_RUNTIME_SERVICE_COMMAND_QUALIFIER = "INIT_RUNTIME",
		ADD_CLIENT_SERVICE_COMMAND_QUALIFIER = "ADD_CLIENT",
		ADD_MATCH_CLIENT_SERVICE_COMMAND_QUALIFIER = "ADD_MATCH_CLIENT",
		DELETE_CLIENT_SERVICE_COMMAND_QUALIFIER = "DELETE_CLIENT",
		HANDLE_MESSAGE_SERVICE_COMMAND_QUALIFIER = "HANDLE_MESSAGE",
		-- Runtime command qualifiers
		RESPOND_CLIENT_RUNTIME_COMMAND_QUALIFIER = "RESPOND_CLIENT",
		SET_ATTRIBUTES_RUNTIME_COMMAND_QUALIFIER = "SET_ATTRIBUTES",
		SET_PROFILE_RUNTIME_COMMAND_QUALIFIER = "SET_PROFILE",
		MULTICAST_MESSAGE_RUNTIME_COMMAND_QUALIFIER = "MULTICAST_MESSAGE",
		BROADCAST_MESSAGE_RUNTIME_COMMAND_QUALIFIER = "BROADCAST_MESSAGE",
		KICK_CLIENT_RUNTIME_COMMAND_QUALIFIER = "KICK_CLIENT",
		REQUEST_MATCHMAKING_RUNTIME_COMMAND_QUALIFIER = "REQUEST_MATCHMAKING",
		STOP_MATCHMAKING_RUNTIME_COMMAND_QUALIFIER = "STOP_MATCHMAKING",
		UPGRADE_CONNECTION_RUNTIME_COMMAND_QUALIFIER = "UPGRADE_CONNECTION",
		-- Misilanious
		UPGRADE_CONNECTION_WEBSOCKET_PROTOCOL = "WEBSOCKET",
	},
	settings = {
		debug = false,
		iterate_interval = 1,
	},
	components = {
		server_state = {
			waiting_for_response = false,
			outgoing_commands = {},
			consumed_commands = {},
			server_events = {},
			iterate_timer = 0,
			-- Methods
			add_outgoing_command = function(server_state, command)
				server_state.outgoing_commands[#server_state.outgoing_commands + 1] = command
			end,
			add_consumed_command = function(server_state, command)
				assert(command.id, "Consumed command must have id")
				server_state.consumed_commands[#server_state.consumed_commands + 1] = command.id
			end,
			add_server_event = function(server_state, event)
				server_state.server_events[#server_state.server_events + 1] = event
			end,
			pull_outgoing_commands = function(server_state)
				local outgoing_commands = server_state.outgoing_commands
				server_state.outgoing_commands = {}
				return outgoing_commands
			end,
			pull_consumed_commands = function(server_state)
				local consumed_commands = server_state.consumed_commands
				server_state.consumed_commands = {}
				return consumed_commands
			end,
			pull_server_events = function(server_state)
				local server_events = server_state.server_events
				server_state.server_events = {}
				return server_events
			end,
		},
		-- Methods
		set_event_handler = function(components, handler)
			components.event_handler = {
				handler = handler
			}
		end,
		set_server_environment = function(components, service_url, user_id, password, runtime_id, runtime_qualifier)
			components.server_environment = {
				service_url = service_url,
				user_id = user_id,
				password = password,
				runtime_id = runtime_id,
				runtime_qualifier = runtime_qualifier,
			}
		end,
		set_service_urls = function(components, service_url)
			components.service_urls = {
				create_token = service_url .. "/omgservers/v1/entrypoint/worker/request/create-token",
				get_config = service_url .. "/omgservers/v1/entrypoint/worker/request/get-config",
				interchange = service_url .. "/omgservers/v1/entrypoint/worker/request/interchange",
				connection = service_url .. "/omgservers/v1/entrypoint/websocket/connection",
			}
		end,
		set_tokens = function(components, api_token, ws_token)
			components.tokens = {
				api_token = api_token,
				ws_token = ws_token,
			}
		end,
		set_config = function(components, version_config)
			components.config = {
				version_config = version_config,
			}
		end,
		set_connection = function(components, ws_connection)
			components.connection = {
				ws_connection = ws_connection,
			}
		end,
	},
	-- Methods
	terminate_server = function(self, code, reason)
		print("[OMGSERVER] Terminated, code=" .. code .. ", reason=" .. reason)
		os.exit(code)
	end,
	build_handler = function(self, callback)
		return function(_, id, response)
			self.components.server_state.waiting_for_response = false

			local response_status = response.status
			local response_body = response.response

			if response_status < 300 then
				if self.settings.debug then
					print("[OMGSERVER] Response, status=" .. response_status .. ", body=" .. response_body)
				end

				local decoded_body
				if response_body then
					decoded_body = json.decode(response_body)
				end

				if callback then
					callback(response_status, decoded_body)
				end
			else
				self:terminate_server(self.constants.API_EXIT_CODE, "api request failed, status=" .. response_status .. ", body=" .. response_body)
			end
		end
	end,
	request_server = function(self, url, request_body, response_handler, request_token)
		assert(self.components.server_state, "Component server_state must be set")

		local request_headers = {
			["Content-Type"] = "application/json"
		}

		if request_token then
			request_headers["Authorization"] = "Bearer " .. request_token
		end

		local is_empty = next(request_body) == nil
		local encoded_body = json.encode(request_body, {
			encode_empty_table_as_object = is_empty
		})

		local method = "PUT"

		if self.settings.debug then
			print("[OMGSERVER] Request, " .. method .. " " .. url .. ", body=" .. encoded_body)
		end

		self.components.server_state.waiting_for_response = true
		http.request(url, method, response_handler, request_headers, encoded_body)
	end,
	create_token = function(self, callback)
		assert(self.components.server_environment, "Component server_environment must be set")
		assert(self.components.service_urls, "Component service_urls must be set")

		local user_id = self.components.server_environment.user_id
		local password = self.components.server_environment.password
		local runtime_id = self.components.server_environment.runtime_id

		local request_body = {
			runtime_id = runtime_id,
			user_id = user_id,
			password = password
		}

		local response_handler = self:build_handler(callback)
		local request_url = self.components.service_urls.create_token
		self:request_server(request_url, request_body, response_handler, nil)
	end,
	get_config = function(self, api_token, callback)
		assert(self.components.server_environment, "Component server_environment must be set")
		assert(self.components.service_urls, "Component service_urls must be set")

		local runtime_id = self.components.server_environment.runtime_id

		local request_body = {
			runtime_id = runtime_id
		}

		local response_handler = self:build_handler(callback)
		local request_url = self.components.service_urls.get_config
		self:request_server(request_url, request_body, response_handler, api_token)
	end,
	ws_connect = function(self, callback)
		assert(self.components.server_environment, "Component server_environment must be set")
		assert(self.components.service_urls, "Component service_urls must be set")
		assert(self.components.tokens, "Component tokens must be set")

		local runtime_id = self.components.server_environment.runtime_id
		local ws_token = self.components.tokens.ws_token

		local connection_url = self.components.service_urls.connection .. "?runtime_id=" .. runtime_id .. "&ws_token=" .. ws_token
		local params = {
			protocol = "omgservers"
		}

		print("[OMGSERVER] Connect websocket, url=" .. connection_url)

		local ws_connection = websocket.connect(connection_url, params, function(_, _, data)
			if data.event == websocket.EVENT_DISCONNECTED then
				self:terminate_server(self.constants.WS_EXIT_CODE, "ws connection disconnected, message=" .. data.message)
			elseif data.event == websocket.EVENT_CONNECTED then
				print("[OMGSERVER] Websocket connected")
				if callback then
					callback()
				end
			elseif data.event == websocket.EVENT_ERROR then
				self:terminate_server(self.constants.WS_EXIT_CODE, "ws connection failed, message=" .. data.message)
			elseif data.event == websocket.EVENT_MESSAGE then
				local decoded_message = json.decode(data.message)
				local client_id = decoded_message.client_id
				local original_message = decoded_message.message
				
				self.components.server_state:add_server_event({
					qualifier = omgserver.constants.MESSAGE_RECEIVED_EVENT_QUALIFIER,
					body = {
						client_id = client_id,
						message = original_message,
					},
				})
			end
		end)

		self.components:set_connection(ws_connection)
	end,
	ws_send = function(self, clients, message)
		assert(omgserver.components.connection, "Connection was not created")

		local encoded_message = json.encode({
			clients = clients,
			message = message,
		})
		
		websocket.send(omgserver.components.connection.ws_connection, encoded_message, {
			type = websocket.DATA_TYPE_TEXT
		})
	end,
	interchange = function(self, api_token, outgoing_commands, consumed_commands, callback)
		assert(self.components.server_environment, "Server environment must be set")
		assert(self.components.service_urls, "Service urls must be set")

		local runtime_id = self.components.server_environment.runtime_id

		local request_body = {
			runtime_id = runtime_id,
			outgoing_commands = outgoing_commands,
			consumed_commands = consumed_commands
		}

		local response_handler = self:build_handler(callback)
		local request_url = self.components.service_urls.interchange
		self:request_server(request_url, request_body, response_handler, api_token)
	end,
	iterate = function(self, api_token)
		local outgoing_commands = self.components.server_state:pull_outgoing_commands()
		local consumed_commands = self.components.server_state:pull_consumed_commands()

		self:interchange(api_token, outgoing_commands, consumed_commands, function(interchange_status, interchange_response)
			local incoming_commands = interchange_response.incoming_commands

			for _, incoming_command in ipairs(incoming_commands) do
				local command_id = incoming_command.id
				local command_qualifier = incoming_command.qualifier
				local command_body = incoming_command.body
				if self.settings.debug then
					print("[OMGSERVER] Handle command, id=" .. string.format("%.0f", command_id) .. ", qualifier=" .. command_qualifier .. ", body=" .. json.encode(command_body))
				end
				self.components.server_state:add_consumed_command(incoming_command)

				self.components.server_state:add_server_event({
					qualifier = omgserver.constants.COMMAND_RECEIVED_EVENT_QUALIFIER,
					body = {
						command_qualifier = command_qualifier,
						command_body = command_body
					}
				})
			end
		end)
	end,	
	update = function(self, dt)
		assert(self.components.event_handler, "Component event_handler must be set")

		if self.components.tokens then
			if self.components.server_state.waiting_for_response then
				-- 
			else
				local server_state = self.components.server_state
				local iterate_interval = omgserver.settings.iterate_interval
				server_state.iterate_timer = server_state.iterate_timer + dt
				if server_state.iterate_timer >= iterate_interval then
					server_state.iterate_timer = server_state.iterate_timer - iterate_interval
					local api_token = self.components.tokens.api_token
					omgserver:iterate(api_token)
				end
			end
		end

		local handler = self.components.event_handler.handler
		local server_events = self.components.server_state:pull_server_events()
		for _, server_event in ipairs(server_events) do
			handler(server_event)
		end
	end,
	init = function(self, handler, debug, interval)
		self.settings.debug = debug or false
		self.settings.iterate_interval = interval or 1
		print("[OMGSERVER] Setting, debug=" .. tostring(self.settings.debug) .. ", interval=" .. self.settings.iterate_interval)

		self.components:set_event_handler(handler)

		local service_url = os.getenv(omgserver.constants.URL_ENVIRONMENT_VARIABLE)
		if not service_url then
			self:terminate_server(self.constants.ENVIRONMENT_EXIT_CODE, "environment variable is nil, variable=service_url")
		end

		local user_id = os.getenv(omgserver.constants.USER_ID_ENVIRONMENT_VARIABLE)
		if not user_id then
			self:terminate_server(self.constants.ENVIRONMENT_EXIT_CODE, "environment variable is nil, variable=user_id")
		end

		local password = os.getenv(omgserver.constants.PASSWORD_ENVIRONMENT_VARIABLE)
		if not password then
			self:terminate_server(self.constants.ENVIRONMENT_EXIT_CODE, "environment variable is nil, variable=password")
		end

		local runtime_id = os.getenv(omgserver.constants.RUNTIME_ID_ENVIRONMENT_VARIABLE)
		if not runtime_id then
			self:terminate_server(self.constants.ENVIRONMENT_EXIT_CODE, "environment variable is nil, variable=runtime_id")
		end

		local runtime_qualifier = os.getenv(omgserver.constants.RUNTIME_QUALIFIER_ENVIRONMENT_VARIABLE)
		if not runtime_qualifier then
			self:terminate_server(self.constants.ENVIRONMENT_EXIT_CODE, "environment variable is nil, variable=runtime_qualifier")
		end

		print("[OMGSERVER] Environment, service_url=" .. service_url)
		print("[OMGSERVER] Environment, user_id=" .. user_id)
		print("[OMGSERVER] Environment, password=" .. string.sub(password, 1, 4) .. "..")
		print("[OMGSERVER] Environment, runtime_id=" .. runtime_id)
		print("[OMGSERVER] Environment, runtime_qualifier=" .. runtime_qualifier)

		self.components:set_server_environment(service_url, user_id, password, runtime_id, runtime_qualifier)
		self.components:set_service_urls(service_url)

		self:create_token(function(create_token_status, create_token_response)
			local api_token = create_token_response.api_token
			local ws_token = create_token_response.ws_token
			self.components:set_tokens(api_token, ws_token)

			self:get_config(api_token, function(get_config_status, get_config_response)
				local version_config = get_config_response.version_config
				self.components:set_config(version_config)

				self:ws_connect(function()
					self.components.server_state:add_server_event({
						qualifier = omgserver.constants.SERVER_INITIALIZED_EVENT_QUALIFIER,
						body = {
							runtime_qualifier = runtime_qualifier,
							version_config = version_config
						}
					})
				end)
			end)
		end)
	end,
}

-- Entrypoint
return {
	constants = omgserver.constants,
	-- Service commands
	service_commands = {
		-- Methods
		set_attributes = function(service_commands, client_id, attributes)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.SET_ATTRIBUTES_RUNTIME_COMMAND_QUALIFIER,
				body = {
					client_id = client_id,
					attributes = {
						attributes = attributes,
					},
				},
			})
		end,
		set_profile = function(service_commands, client_id, profile)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.SET_PROFILE_RUNTIME_COMMAND_QUALIFIER,
				body = {
					client_id = client_id,
					profile = profile,
				},
			})
		end,
		respond_client = function(service_commands, client_id, message)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.RESPOND_CLIENT_RUNTIME_COMMAND_QUALIFIER,
				body = {
					client_id = client_id,
					message = message,
				},
			})
		end,
		multicast_message = function(service_commands, clients, message)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.MULTICAST_MESSAGE_RUNTIME_COMMAND_QUALIFIER,
				body = {
					clients = clients,
					message = message,
				},
			})
		end,
		broadcast_message = function(service_commands, message)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.BROADCAST_MESSAGE_RUNTIME_COMMAND_QUALIFIER,
				body = {
					message = message,
				},
			})
		end,
		kick_client = function(service_commands, client_id)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.KICK_CLIENT_RUNTIME_COMMAND_QUALIFIER,
				body = {
					client_id = client_id,
				},
			})
		end,
		request_matchmaking = function(service_commands, client_id, mode)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.REQUEST_MATCHMAKING_RUNTIME_COMMAND_QUALIFIER,
				body = {
					client_id = client_id,
					mode = mode,
				},
			})
		end,
		stop_matchmaking = function(service_commands, reason)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.STOP_MATCHMAKING_RUNTIME_COMMAND_QUALIFIER,
				body = {
					reason = reason,
				},
			})
		end,
		upgrade_connection = function(service_commands, client_id)
			omgserver.components.server_state:add_outgoing_command({
				qualifier = omgserver.constants.UPGRADE_CONNECTION_RUNTIME_COMMAND_QUALIFIER,
				body = {
					client_id = client_id,
					protocol = omgserver.constants.UPGRADE_CONNECTION_WEBSOCKET_PROTOCOL,
				},
			})
		end,
	},
	connections = {
		respond_text_message = function(connections, client_id, message)
			assert(omgserver.components.connection, "Connection was not created")
			omgserver:ws_send({ client_id }, message)
		end,
		multicast_text_message = function(connections, clients, message)
			assert(omgserver.components.connection, "Connection was not created")
			omgserver:ws_send(clients, message)
		end,
		broadcast_text_message = function(connections, message)
			assert(omgserver.components.connection, "Connection was not created")
			omgserver:ws_send(nil, message)
		end,
	},
	-- Methods
	init = function(self, handler, debug, interval)
		omgserver:init(handler, debug, interval)
	end,
	get_qualifier = function(self)
		assert(omgserver.components.server_environment, "Server was not initialized")
		return omgserver.components.server_environment.runtime_qualifier
	end,
	get_config = function(self)
		assert(omgserver.components.config, "Server was not initialized")
		return omgserver.components.config.version_config
	end,
	update = function(self, dt)
		omgserver:update(dt)
	end,
}