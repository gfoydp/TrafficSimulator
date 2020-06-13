package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private final static String _modeConsole = "console";
	private final static String _modeGUI = "gui";
	private static Factory<Event> _eventsFactory = null;
	private static int time;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTimeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg()
				.desc("Execution Mode. Possible values: 'console' (Console mode), 'gui' (Graphical User Interface mode). Default value: '"
						+ _modeGUI + "'.")
				.build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator’s main loop (default value is 10)").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		_mode = line.getOptionValue("m", _modeGUI);
		if(!_mode.equals(_modeConsole) && !_mode.equals(_modeGUI)) throw new ParseException("Invalid mode value: " + _mode);
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && (_mode == null || _mode.equals(_modeConsole))) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTimeOption(CommandLine line) throws ParseException {
		if(line.hasOption("t"))
		time = Integer.parseInt(line.getOptionValue("t"));
		else time =  _timeLimitDefaultValue;
	}

	private static void initFactories() {
		
		List<Builder<LightSwitchingStrategy>> lss = new ArrayList<>();
		
		lss.add(new RoundRobinStrategyBuilder("round_robin_lss"));
		lss.add(new MostCrowdedStrategyBuilder("most_crowded_lss"));
		Factory<LightSwitchingStrategy> ls = new BuilderBasedFactory<>(lss);
		
		List<Builder<DequeuingStrategy>> dqs = new ArrayList<>();
		dqs.add(new MoveFirstStrategyBuilder("move_first_dqs"));
		dqs.add(new MoveAllStrategyBuilder("most_all_dqs"));
		Factory<DequeuingStrategy> ds = new BuilderBasedFactory<DequeuingStrategy>(dqs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder("new_junction", ls, ds));
		ebs.add(new NewInterCityRoadEventBuilder("new_inter_city_road"));
		ebs.add(new NewCityRoadEventBuilder("new_city_road"));
		ebs.add(new NewVehicleEventBuilder("new_vehicle"));
		ebs.add(new SetWeatherEventBuilder("set_weather"));
		ebs.add(new SetContClassEventBuilder("set_cont_class"));
		
		_eventsFactory = new BuilderBasedFactory<>(ebs);
		//TODO complete this method to initialize _eventsFactory
	}

	private static void startBatchMode() throws IOException {
		InputStream in = new FileInputStream(_inFile);
		OutputStream out;
		if(_outFile == null)
			out = System.out;
		else out = new FileOutputStream(_outFile);
		TrafficSimulator simulator = new TrafficSimulator();
		Controller controller = new Controller(simulator, _eventsFactory);
		controller.loadEvents(in);
		controller.run(time, out); 
		in.close();
		System.out.println();
		if(_outFile == null)
		System.out.println("Operacion realizada correctamente.");
		else System.out.println("El archivo .json se ha guardado correctamente en : " +  _outFile);
		
		// TODO complete this method to start the simulation
		
	}
	private static void startGUIMode() throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
			TrafficSimulator simulator = new TrafficSimulator();
			Controller ctrl = new Controller(simulator, _eventsFactory);
			new MainWindow(ctrl);
			if(_inFile != null) {
				InputStream in;
				try {
					in = new FileInputStream(_inFile);
					ctrl.loadEvents(in);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			}
			});

	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_mode.equals(_modeConsole)) startBatchMode();
		else if(_mode.equals(_modeGUI)) startGUIMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help
	// -m gui

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
