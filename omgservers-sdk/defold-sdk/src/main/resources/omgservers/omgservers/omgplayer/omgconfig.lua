local omgconfig
omgconfig = {
	--[[
		self,
		options = {
			-- Required
			tenant_id,
			stage_id,
			stage_secret,
			event_handler,
			-- Optional
			service_url,
			debug_logging,
			trace_logging,
			default_interval,
			faster_interval,
			iterations_threshold,
		},
	]]--
	create = function(self, options)
		assert(self, "The self must not be nil.")
		assert(options, "The options must not be nil.")
		assert(options.tenant_id, "The value tenant_id must not be nil.")
		assert(options.stage_id, "The value stage_id must not be nil.")
		assert(options.stage_secret, "The value stage_secret must not be nil.")
		assert(options.event_handler, "The value event_handler must not be null.")

		local service_url = options.service_url or "https://api.omgservers.com"
		local debug_logging = options.debug_logging or false
		local trace_logging = options.trace_logging or false
		local default_interval = options.default_interval or 2
		local faster_interval = options.faster_interval or 0.5
		local iterations_threshold = options.iterations_threshold or 12

		assert(default_interval > 0, "The default interval must be greater than zero.")
		assert(faster_interval > 0, "The faster interval must be greater than zero.")
		assert(iterations_threshold > 0, "The iteration threshold must be greater than zero.")
		assert(faster_interval < default_interval, "The faster interval must be less than the default.")
		
		local instance = {
			type = "omgconfig",
			tenant_id = options.tenant_id,
			stage_id = options.stage_id,
			stage_secret = options.stage_secret,
			event_handler = options.event_handler,
			service_url = service_url,
			debug_logging = debug_logging,
			trace_logging = trace_logging,
			default_interval = default_interval,
			faster_interval = faster_interval,
			iterations_threshold = iterations_threshold,
		}

		if debug_logging then
			print(socket.gettime() .. " [OMGPLAYER] Config was created")
			pprint(instance)
		end
		
		return instance
	end,
}

return omgconfig
